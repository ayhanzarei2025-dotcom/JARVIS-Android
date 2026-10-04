package com.jarvis.ai
import android.content.Context
import dev.ffmpegkit.llama.Llama
import dev.ffmpegkit.llama.LlamaConfig
import kotlinx.coroutines.*
import java.io.File

class OfflineLlmEngine(private val context: Context) {
    interface Callback { fun done(text: String) }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var model: dev.ffmpegkit.llama.LlamaModel? = null
    private var loading = false
    private val modelFile = File(context.filesDir, "models/jarvis-qwen-q4.gguf")

    fun prepare(callback: ((Boolean, String) -> Unit)? = null) {
        if (model?.isLoaded == true) { callback?.invoke(true, "آماده"); return }
        if (loading) { callback?.invoke(false, "در حال بارگذاری"); return }
        loading = true
        scope.launch {
            try {
                if (!modelFile.exists()) {
                    modelFile.parentFile?.mkdirs()
                    context.assets.open("offline/jarvis-qwen-q4.gguf").use { input ->
                        modelFile.outputStream().use { output -> input.copyTo(output, 1024 * 1024) }
                    }
                }
                model = Llama.loadModel(modelFile.absolutePath,
                    LlamaConfig(contextSize = 3072, threads = 6, gpuLayers = 12, temperature = 0.55f, topP = 0.9f, topK = 40))
                withContext(Dispatchers.Main) { callback?.invoke(true, "مدل آفلاین آماده است • GPU فعال") }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { callback?.invoke(false, "مدل آفلاین بارگذاری نشد: " + (e.message ?: "خطای نامشخص")) }
            } finally { loading = false }
        }
    }
    fun ask(question: String, memory: String, callback: Callback) {
        prepare { ok, msg ->
            if (!ok) { callback.done(msg); return@prepare }
            scope.launch {
                try {
                    val result = Llama.complete(model!!, question,
                        """تو JARVIS 2.01 هستی، دستیار فارسی کاربر.
همیشه فارسی پاسخ بده مگر کاربر صریحاً زبان دیگری بخواهد.
پاسخ طبیعی، دقیق و نسبتاً کوتاه بده.
درخواست‌های قتل، ساخت سلاح، ساخت مواد منفجره یا محتوای جنسی صریح را انجام نده.
حافظه مرتبط:
$memory""", 448)
                    withContext(Dispatchers.Main) { callback.done(result.text.trim()) }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) { callback.done("پاسخ آفلاین ناموفق بود: " + (e.message ?: "خطای مدل")) }
                }
            }
        }
    }
    fun release() { model?.let { Llama.releaseModel(it) }; model = null; scope.cancel() }
}