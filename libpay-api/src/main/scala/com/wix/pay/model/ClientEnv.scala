package com.wix.pay.model

/**
  * The client environment in which payment operation is executed.
  *
  * @param userAgent the user agent in use by the cardholder.
  * @param referrer  the URL of the page containing the payment form.
  * @param deviceId  the fingerprint for the user's device or browser.
  */
case class ClientEnv(userAgent: Option[String] = None,
                     referrer: Option[String] = None,
                     deviceId: Option[String] = None)
