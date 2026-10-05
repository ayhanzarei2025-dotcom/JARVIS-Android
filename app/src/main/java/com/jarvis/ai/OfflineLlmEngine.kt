package com.jarvis.ai
import android.content.Context
import dev.ffmpegkit.llama.Llama
import dev.ffmpegkit.llama.LlamaConfig
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class OfflineLlmEngine(private val context: Context) {
    interface Callback { fun done(text: String) }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var model: dev.ffmpegkit.llama.LlamaModel? = null
    private var loading = false
    private val modelDir = File(context.filesDir, "models")
    private val modelFile = File(modelDir, "jarvis-qwen-q4.gguf")
    companion object {
        private const val MODEL_URL = "https://huggingface.co/Qwen/Qwen2.5-0.5B-Instruct-GGUF/resolve/main/qwen2.5-0.5b-instruct-q4_0.gguf"
        private const val MODEL_MIN = 350_000_000L
        private const val MODEL_MAX = 470_000_000L
    }
    fun prepare(callback: ((Boolean, String) -> Unit)? = null) {
        if (model?.isLoaded == true) { callback?.invoke(true, "آماده"); return }
        if (loading) { callback?.invoke(false, "در حال آماده‌سازی مغز محلی…"); return }
        loading = true
        scope.launch {
            try {
                modelDir.mkdirs()
                if (!modelFile.exists() || modelFile.length() !in MODEL_MIN..MODEL_MAX) downloadModel()
                model = Llama.loadModel(modelFile.absolutePath, LlamaConfig(contextSize = 3072, threads = 6, gpuLayers = 12, temperature = 0.55f, topP = 0.9f, topK = 40))
                withContext(Dispatchers.Main) { callback?.invoke(true, "مغز محلی آماده است • GPU target فعال") }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { callback?.invoke(false, "آماده‌سازی مغز محلی ناموفق بود: " + (e.message ?: "خطای نامشخص")) }
            } finally { loading = false }
        }
    }
    private fun downloadModel() {
        val tmp = File(modelDir, "jarvis-qwen-q4.gguf.part")
        var c: HttpURLConnection? = null
        try {
            c = (URL(MODEL_URL).openConnection() as HttpURLConnection).apply { connectTimeout=20_000; readTimeout=120_000; instanceFollowRedirects=true; setRequestProperty("User-Agent", "JARVIS-Android/2.02") }
            c.connect()
            if (c.responseCode !in 200..299) error("دانلود مدل با کد ${c.responseCode} شکست خورد")
            FileOutputStream(tmp).use { out -> c.inputStream.use { input ->
                val buf = ByteArray(1024 * 1024); var total=0L
                while (true) { val n=input.read(buf); if (n<=0) break; out.write(buf,0,n); total+=n; if(total>MODEL_MAX) error("حجم مدل بیش از حد مجاز است") }
            }}
            if (tmp.length() !in MODEL_MIN..MODEL_MAX) error("دانلود ناقص یا نامعتبر است")
            if (modelFile.exists()) modelFile.delete()
            if (!tmp.renameTo(modelFile)) error("ذخیره مدل ناموفق بود")
        } finally { c?.disconnect(); if(tmp.exists() && !modelFile.exists()) tmp.delete() }
    }
    fun ask(question: String, memory: String, callback: Callback) {
        prepare { ok, msg -> if (!ok) { callback.done(msg); return@prepare }; scope.launch {
            try {
                val result = Llama.complete(model!!, question, """تو JARVIS 2.02 هستی، دستیار فارسی کاربر.
همیشه فارسی پاسخ بده مگر کاربر صریحاً زبان دیگری بخواهد.
پاسخ طبیعی، دقیق و نسبتاً کوتاه بده.
درخواست‌های قتل، ساخت سلاح، ساخت مواد منفجره یا محتوای جنسی صریح را انجام نده.
حافظه مرتبط:
$memory""", 448)
                withContext(Dispatchers.Main) { callback.done(result.text.trim()) }
            } catch (e: Exception) { withContext(Dispatchers.Main) { callback.done("پاسخ آفلاین ناموفق بود: " + (e.message ?: "خطای مدل")) } }
        }}
    }
    fun release() { model?.let { Llama.releaseModel(it) }; model=null; scope.cancel() }
}