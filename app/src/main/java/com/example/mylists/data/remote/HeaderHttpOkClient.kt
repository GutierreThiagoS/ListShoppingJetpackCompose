package com.example.mylists.data.remote

import android.annotation.SuppressLint
import android.util.Base64
import android.util.Log
import okhttp3.OkHttpClient
import org.koin.android.BuildConfig
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object HeaderHttpOkClient {

    private const val user = "AppMerchant"
    private const val password = "T4NT0f4z"

//    const val BASE_URL_BAR = "https://api.mercadolibre.com"
    const val BASE_URL_BAR_JSL = "https://api-b2b.guarany.com.br/"

    private const val certificate = "-----BEGIN CERTIFICATE-----\n" +
            "MIIGIzCCBQugAwIBAgIQC2NuY2wyptfHv30GkIzALDANBgkqhkiG9w0BAQsFADBP\n" +
            "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMSkwJwYDVQQDEyBE\n" +
            "aWdpQ2VydCBUTFMgUlNBIFNIQTI1NiAyMDIwIENBMTAeFw0yMzA2MjUwMDAwMDBa\n" +
            "Fw0yNDA2MjUyMzU5NTlaMIGSMQswCQYDVQQGEwJCUjEPMA0GA1UECAwGQ2VhcsOh\n" +
            "MRIwEAYDVQQHEwlGb3J0YWxlemExQzBBBgNVBAoTOkogU0xFSU1BTiBTLkEuIENP\n" +
            "TUVSQ0lPIERFIFBST0RVVE9TIERFIEhJR0lFTkUgRSBBTElNRU5UT1MxGTAXBgNV\n" +
            "BAMMECouZ3VhcmFueS5jb20uYnIwWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAATA\n" +
            "Cgmq7tnF2z6ZJPG/AxmOYOaGxoQ8/7v5b4CpyTiSY+87HwESn/dvOKSCFvjp9LsW\n" +
            "OXKxordCFgdSt71IXOQXo4IDgDCCA3wwHwYDVR0jBBgwFoAUt2ui6qiqhIx56rTa\n" +
            "D5iyxZV2ufQwHQYDVR0OBBYEFJX3cfPZoPHIVNeK3zcha4by2hg6MCsGA1UdEQQk\n" +
            "MCKCECouZ3VhcmFueS5jb20uYnKCDmd1YXJhbnkuY29tLmJyMA4GA1UdDwEB/wQE\n" +
            "AwIHgDAdBgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwgY8GA1UdHwSBhzCB\n" +
            "hDBAoD6gPIY6aHR0cDovL2NybDMuZGlnaWNlcnQuY29tL0RpZ2lDZXJ0VExTUlNB\n" +
            "U0hBMjU2MjAyMENBMS00LmNybDBAoD6gPIY6aHR0cDovL2NybDQuZGlnaWNlcnQu\n" +
            "Y29tL0RpZ2lDZXJ0VExTUlNBU0hBMjU2MjAyMENBMS00LmNybDA+BgNVHSAENzA1\n" +
            "MDMGBmeBDAECAjApMCcGCCsGAQUFBwIBFhtodHRwOi8vd3d3LmRpZ2ljZXJ0LmNv\n" +
            "bS9DUFMwfwYIKwYBBQUHAQEEczBxMCQGCCsGAQUFBzABhhhodHRwOi8vb2NzcC5k\n" +
            "aWdpY2VydC5jb20wSQYIKwYBBQUHMAKGPWh0dHA6Ly9jYWNlcnRzLmRpZ2ljZXJ0\n" +
            "LmNvbS9EaWdpQ2VydFRMU1JTQVNIQTI1NjIwMjBDQTEtMS5jcnQwCQYDVR0TBAIw\n" +
            "ADCCAX4GCisGAQQB1nkCBAIEggFuBIIBagFoAHYA7s3QZNXbGs7FXLedtM0TojKH\n" +
            "Rny87N7DUUhZRnEftZsAAAGI7+P0egAABAMARzBFAiEAvKOA/HpBp+RzgsRLc3em\n" +
            "FDVdD5BiV1qK9okAVHBhMcsCIHKDy8INqUOEZa3jFRz2sf6zlWzWDqdj1+ignqJQ\n" +
            "+zIbAHYASLDja9qmRzQP5WoC+p0w6xxSActW3SyB2bu/qznYhHMAAAGI7+P0PQAA\n" +
            "BAMARzBFAiBdsjaUi7CtCsGAlH5q2M/irteDO33B54CEI1GKQD7qvgIhANdk23qE\n" +
            "nvnByXXdMuDWmR18CG0FO+Vr62HzTmEdQq+3AHYA2ra/az+1tiKfm8K7XGvocJFx\n" +
            "bLtRhIU0vaQ9MEjX+6sAAAGI7+Pz0QAABAMARzBFAiBiWMzP6I5wrMBipM7mb5cI\n" +
            "rpbdUgWfmUyU0WUWhPLN0AIhAOTjg6NpCNpz0F7yCnLobPMLl1ZO0upwp6cnFbcJ\n" +
            "r+4JMA0GCSqGSIb3DQEBCwUAA4IBAQCaBt9jV2G5iGMUn8RIfu0BZ8DOCTJF8CPF\n" +
            "LAUkzY24yRRZU0sQDe0Ez1fIHUtchBdWxd4MaajwGwXhHeb13ynoU/Vo2nyCXdZS\n" +
            "vkUmcSlkh3jVsWjIuK0fa9U1JRB/X7vqFrJlPwTjKq5DlRKQ5CcoMY7GIqdJ4ix3\n" +
            "MG5Jmu5BNKW/ejBxHmtRaC0f28fv7VTkzeKOMIvRthQUKATReBmjLY0fRHSsSZMo\n" +
            "fPLRDEpMdhMf6Kn7HyH3i2KRNPL1/qS4N3m+sRbNthNpjzqpkL1jnPMw9p08f+EO\n" +
            "/QJbadmGPqiiMPZz8IXsEiN3swbU+kGDUCUGO8kwyAXXiMma+pgO\n" +
            "-----END CERTIFICATE-----\n" +
            "-----BEGIN CERTIFICATE-----\n" +
            "MIIEvjCCA6agAwIBAgIQBtjZBNVYQ0b2ii+nVCJ+xDANBgkqhkiG9w0BAQsFADBh\n" +
            "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\n" +
            "d3cuZGlnaWNlcnQuY29tMSAwHgYDVQQDExdEaWdpQ2VydCBHbG9iYWwgUm9vdCBD\n" +
            "QTAeFw0yMTA0MTQwMDAwMDBaFw0zMTA0MTMyMzU5NTlaME8xCzAJBgNVBAYTAlVT\n" +
            "MRUwEwYDVQQKEwxEaWdpQ2VydCBJbmMxKTAnBgNVBAMTIERpZ2lDZXJ0IFRMUyBS\n" +
            "U0EgU0hBMjU2IDIwMjAgQ0ExMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKC\n" +
            "AQEAwUuzZUdwvN1PWNvsnO3DZuUfMRNUrUpmRh8sCuxkB+Uu3Ny5CiDt3+PE0J6a\n" +
            "qXodgojlEVbbHp9YwlHnLDQNLtKS4VbL8Xlfs7uHyiUDe5pSQWYQYE9XE0nw6Ddn\n" +
            "g9/n00tnTCJRpt8OmRDtV1F0JuJ9x8piLhMbfyOIJVNvwTRYAIuE//i+p1hJInuW\n" +
            "raKImxW8oHzf6VGo1bDtN+I2tIJLYrVJmuzHZ9bjPvXj1hJeRPG/cUJ9WIQDgLGB\n" +
            "Afr5yjK7tI4nhyfFK3TUqNaX3sNk+crOU6JWvHgXjkkDKa77SU+kFbnO8lwZV21r\n" +
            "eacroicgE7XQPUDTITAHk+qZ9QIDAQABo4IBgjCCAX4wEgYDVR0TAQH/BAgwBgEB\n" +
            "/wIBADAdBgNVHQ4EFgQUt2ui6qiqhIx56rTaD5iyxZV2ufQwHwYDVR0jBBgwFoAU\n" +
            "A95QNVbRTLtm8KPiGxvDl7I90VUwDgYDVR0PAQH/BAQDAgGGMB0GA1UdJQQWMBQG\n" +
            "CCsGAQUFBwMBBggrBgEFBQcDAjB2BggrBgEFBQcBAQRqMGgwJAYIKwYBBQUHMAGG\n" +
            "GGh0dHA6Ly9vY3NwLmRpZ2ljZXJ0LmNvbTBABggrBgEFBQcwAoY0aHR0cDovL2Nh\n" +
            "Y2VydHMuZGlnaWNlcnQuY29tL0RpZ2lDZXJ0R2xvYmFsUm9vdENBLmNydDBCBgNV\n" +
            "HR8EOzA5MDegNaAzhjFodHRwOi8vY3JsMy5kaWdpY2VydC5jb20vRGlnaUNlcnRH\n" +
            "bG9iYWxSb290Q0EuY3JsMD0GA1UdIAQ2MDQwCwYJYIZIAYb9bAIBMAcGBWeBDAEB\n" +
            "MAgGBmeBDAECATAIBgZngQwBAgIwCAYGZ4EMAQIDMA0GCSqGSIb3DQEBCwUAA4IB\n" +
            "AQCAMs5eC91uWg0Kr+HWhMvAjvqFcO3aXbMM9yt1QP6FCvrzMXi3cEsaiVi6gL3z\n" +
            "ax3pfs8LulicWdSQ0/1s/dCYbbdxglvPbQtaCdB73sRD2Cqk3p5BJl+7j5nL3a7h\n" +
            "qG+fh/50tx8bIKuxT8b1Z11dmzzp/2n3YWzW2fP9NsarA4h20ksudYbj/NhVfSbC\n" +
            "EXffPgK2fPOre3qGNm+499iTcc+G33Mw+nur7SpZyEKEOxEXGlLzyQ4UfaJbcme6\n" +
            "ce1XR2bFuAJKZTRei9AqPCCcUZlM51Ke92sRKw2Sfh3oius2FkOH6ipjv3U/697E\n" +
            "A7sKPPcw7+uvTPyLNhBzPvOk\n" +
            "-----END CERTIFICATE-----\n" +
            "-----BEGIN CERTIFICATE-----\n" +
            "MIIDrzCCApegAwIBAgIQCDvgVpBCRrGhdWrJWZHHSjANBgkqhkiG9w0BAQUFADBh\n" +
            "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\n" +
            "d3cuZGlnaWNlcnQuY29tMSAwHgYDVQQDExdEaWdpQ2VydCBHbG9iYWwgUm9vdCBD\n" +
            "QTAeFw0wNjExMTAwMDAwMDBaFw0zMTExMTAwMDAwMDBaMGExCzAJBgNVBAYTAlVT\n" +
            "MRUwEwYDVQQKEwxEaWdpQ2VydCBJbmMxGTAXBgNVBAsTEHd3dy5kaWdpY2VydC5j\n" +
            "b20xIDAeBgNVBAMTF0RpZ2lDZXJ0IEdsb2JhbCBSb290IENBMIIBIjANBgkqhkiG\n" +
            "9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4jvhEXLeqKTTo1eqUKKPC3eQyaKl7hLOllsB\n" +
            "CSDMAZOnTjC3U/dDxGkAV53ijSLdhwZAAIEJzs4bg7/fzTtxRuLWZscFs3YnFo97\n" +
            "nh6Vfe63SKMI2tavegw5BmV/Sl0fvBf4q77uKNd0f3p4mVmFaG5cIzJLv07A6Fpt\n" +
            "43C/dxC//AH2hdmoRBBYMql1GNXRor5H4idq9Joz+EkIYIvUX7Q6hL+hqkpMfT7P\n" +
            "T19sdl6gSzeRntwi5m3OFBqOasv+zbMUZBfHWymeMr/y7vrTC0LUq7dBMtoM1O/4\n" +
            "gdW7jVg/tRvoSSiicNoxBN33shbyTApOB6jtSj1etX+jkMOvJwIDAQABo2MwYTAO\n" +
            "BgNVHQ8BAf8EBAMCAYYwDwYDVR0TAQH/BAUwAwEB/zAdBgNVHQ4EFgQUA95QNVbR\n" +
            "TLtm8KPiGxvDl7I90VUwHwYDVR0jBBgwFoAUA95QNVbRTLtm8KPiGxvDl7I90VUw\n" +
            "DQYJKoZIhvcNAQEFBQADggEBAMucN6pIExIK+t1EnE9SsPTfrgT1eXkIoyQY/Esr\n" +
            "hMAtudXH/vTBH1jLuG2cenTnmCmrEbXjcKChzUyImZOMkXDiqw8cvpOp/2PV5Adg\n" +
            "06O/nVsJ8dWO41P0jmP6P6fbtGbfYmbW0W5BjfIttep3Sp+dWOIrWcBAI+0tKIJF\n" +
            "PnlUkiaY4IBIqDfv8NZ5YBberOgOzW6sRBc4L0na4UU+Krk2U886UAb3LujEV0ls\n" +
            "YSEY1QSteDwsOoBrp+uvFRTp2InBuThs4pFsiv9kuXclVzDAGySj4dzp30d8tbQk\n" +
            "CAUw7C29C79Fv1C5qfPrmAESrciIxpg0X40KPMbp1ZWVbd4=\n" +
            "-----END CERTIFICATE-----"

    private lateinit var httpClient: OkHttpClient

    fun getHeaderOkHttpClient(): OkHttpClient {
        if (HeaderHttpOkClient::httpClient.isInitialized) {
            return httpClient
        } else {
            val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            val ks = KeyStore.getInstance(KeyStore.getDefaultType())
            val certificateFactory = CertificateFactory.getInstance("X.509")
            val certificate =
                certificateFactory.generateCertificate(ByteArrayInputStream(certificate.toByteArray()))
            ks.load(null, null)
            ks.setCertificateEntry("ca", certificate)
            tmf.init(ks)
            val httpClient = OkHttpClient().newBuilder()
            httpClient.sslSocketFactory(
                getCertification().socketFactory, tmf.trustManagers[0] as X509TrustManager
            )
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .callTimeout(1, TimeUnit.MINUTES)
                .retryOnConnectionFailure(false)
                .addNetworkInterceptor { chain ->
                    val original = chain.request()
                    val originalHttpUrl = original.url()

                    val url = originalHttpUrl.newBuilder()
                        .addQueryParameter("charset", "utf-8")
                        .addQueryParameter("Content-Type", "application/json")
                        .addQueryParameter("Accept", "*/*")
//                        .addQueryParameter("tenantId", viewModel.findAppContextScope()?.tenantID)
                        .addQueryParameter("Authorization", getAuthorization())
                        .build()

                    chain.proceed(
                        original.newBuilder().url(url).build()
                    )
                }

            return httpClient.build()
        }
    }
    @SuppressLint("CustomX509TrustManager")
    private fun getCertification(): SSLContext {

        val caInput: InputStream = certificate.byteInputStream()
        val certificateFactory = CertificateFactory.getInstance("X.509")
        var certificate: Certificate? = null

        try {
            certificate = certificateFactory.generateCertificate(caInput)
        } catch (e: Exception) {
            Log.e("Exception", "$e")
            e.printStackTrace()
        } finally {
            caInput.close()
        }

        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)

        if (certificate != null) {
            keyStore.setCertificateEntry("ca", certificate)
        }

        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm)
        trustManagerFactory.init(keyStore)

        val sslContext = SSLContext.getInstance("TLS")
        if (BuildConfig.DEBUG) {
            sslContext.init(null, arrayOf(
                object : X509TrustManager {
                    @SuppressLint("TrustAllX509TrustManager")
                    override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {

                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {

                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }

                }), null)
        } else {
            sslContext.init(null, trustManagerFactory.trustManagers, null)
        }

        return sslContext
    }

    fun getAuthorization(): String {
        val source = "$user:$password"
        return "Basic " + Base64.encodeToString(source.toByteArray(Charsets.UTF_8), Base64.URL_SAFE or Base64.NO_WRAP)
    }
}