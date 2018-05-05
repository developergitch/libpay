package com.wix.pay.testkit

import java.util.Locale

import com.wix.pay.creditcard._
import com.wix.pay.model._

trait LibPayTestSupport {
  val someCurrencyAmount = CurrencyAmount("USD", 5.67)

  val somePayment = Payment(someCurrencyAmount, installments = 1)

  private val someBillingAddress = AddressDetailed(
    street = Some("Some Billing Street"),
    city = Some("Some Billing City"),
    state = Some("someBillingState"),
    postalCode = Some("12345"),
    countryCode = Some(Locale.GERMANY)
  )

  private val somePublicCreditCardOptionalFields = PublicCreditCardOptionalFields(
    holderId = Some("someHolderId"),
    holderName = Some("Some Holder Name"),
    billingAddressDetailed = Some(someBillingAddress)
  )

  private val someCreditCardOptionalFields = CreditCardOptionalFields(
    csc = Some("123"),
    publicFields = Some(somePublicCreditCardOptionalFields)
  )

  val someCreditCard = CreditCard(
    number = "4580458045804580",
    expiration = YearMonth(2020, 12),
    additionalFields = Some(someCreditCardOptionalFields)
  )

  private val someCustomerName = Name(
    first = "Some First Name",
    last = "Some Last Name"
  )

  private val someClientEnv = ClientEnv(
    userAgent = Some("Some User Agent"),
    referrer = Some("https://some.business.com/pay"),
    deviceId = Some("some-device-id")
  )

  val someCustomer = Customer(
    phone = Some("123456789"),
    email = Some("some@email.com"),
    name = Some(someCustomerName),
    ipAddress = Some("111.222.333.444"),
    fax = Some("987654321"),
    company = Some("Some Customer Company"),
    buyerId = Some("some-buyer-id"),
    clientEnv = Some(someClientEnv)
  )

  private val someShippingAddressDetailed = AddressDetailed(
    street = Some("Some Shipping Street"),
    city = Some("Some Shipping City"),
    state = Some("someShippingState"),
    postalCode = Some("54321"),
    countryCode = Some(Locale.FRANCE)
  )

  private val someShippingAddress = ShippingAddress(
    firstName = Some("Some Shipping First Name"),
    lastName = Some("Some Shipping Last Name"),
    address = Some(someShippingAddressDetailed)
  )

  private val someOrderItem = OrderItem(
    id = Some("someItemId"),
    name = Some("Some Item Name"),
    quantity = Some(1.0),
    pricePerItem = Some(2.3),
    description = Some("Some Item Description")
  )
  
  private val someIncludedCharges = IncludedCharges(
    tax = Some(4.5),
    shipping = Some(6.7)
  )
  
  val someDeal = Deal(
    id = "someDealId",
    title = Some("Some Deal Title"),
    description = Some("Some Deal Description"),
    invoiceId = Some("someInvoiceId"),
    shippingAddress = Some(someShippingAddress),
    orderItems = Seq(someOrderItem),
    includedCharges = Some(someIncludedCharges)
  )

  implicit class PaymentTestExtensions(o: Payment) {
    def currency = o.currencyAmount.currency
    def amount = o.currencyAmount.amount
    
    def withInstallments(installments: Int): Payment = o.copy(installments = installments)

    def withCurrency(currency: String): Payment = o.copy(currencyAmount = o.currencyAmount.withCurrency(currency))
    def withAmount(amount: Double): Payment = o.copy(currencyAmount = o.currencyAmount.withAmount(amount))
  }
  
  implicit class CurrencyAmountTestExtensions(o: CurrencyAmount) {
    def withCurrency(currency: String): CurrencyAmount = o.copy(currency = currency)
    def withAmount(amount: Double): CurrencyAmount = o.copy(amount = amount)
  }
  
  implicit class CreditCardTestExtensions(o: CreditCard) {
    def expirationMonth = o.expiration.month
    def expirationYear = o.expiration.year
    
    def withNumber(number: String): CreditCard = o.copy(number = number)

    def withExpirationMonth(month: Int): CreditCard = o.copy(expiration = o.expiration.withMonth(month))
    def withExpirationYear(year: Int): CreditCard = o.copy(expiration = o.expiration.withYear(year))

    def withoutCsc: CreditCard = withOptionalFields(_.withoutCsc)
    def withCsc(csc: String): CreditCard = withOptionalFields(_.withCsc(csc))
    def withCsc(csc: Option[String]): CreditCard = withOptionalFields(_.withCsc(csc))

    def withoutHolderId: CreditCard = withOptionalFields(_.withoutHolderId)
    def withHolderId(holderId: String): CreditCard = withOptionalFields(_.withHolderId(holderId))
    def withHolderId(holderId: Option[String]): CreditCard = withOptionalFields(_.withHolderId(holderId))

    def withoutHolderName: CreditCard = withOptionalFields(_.withoutHolderName)
    def withHolderName(holderName: String): CreditCard = withOptionalFields(_.withHolderName(holderName))
    def withHolderName(holderName: Option[String]): CreditCard = withOptionalFields(_.withHolderName(holderName))

    def withoutBillingAddress: CreditCard = withOptionalFields(_.withoutBillingAddress)
    def withBillingAddress(copier: AddressDetailed => AddressDetailed): CreditCard = withOptionalFields(_.withBillingAddress(copier))

    def withoutOptionalFields: CreditCard = withOptionalFields(None)
    def withOptionalFields(copier: CreditCardOptionalFields => CreditCardOptionalFields): CreditCard =
      withOptionalFields(Some(copier(o.additionalFields.getOrElse(someCreditCardOptionalFields))))
    def withOptionalFields(optionalFields: Option[CreditCardOptionalFields]): CreditCard =
      o.copy(additionalFields = optionalFields)
  }

  implicit class YearMonthTestExtensions(o: YearMonth) {
    def withMonth(month: Int): YearMonth = o.copy(month = month)
    def withYear(year: Int): YearMonth = o.copy(year = year)
  }

  implicit class CreditCardOptionalFieldsTestExtensions(o: CreditCardOptionalFields) {
    def withoutCsc: CreditCardOptionalFields = withCsc(None)
    def withCsc(csc: String): CreditCardOptionalFields = withCsc(Some(csc))
    def withCsc(csc: Option[String]): CreditCardOptionalFields = o.copy(csc = csc)

    def withoutHolderId: CreditCardOptionalFields = withPublicFields(_.withoutHolderId)
    def withHolderId(holderId: String): CreditCardOptionalFields = withPublicFields(_.withHolderId(holderId))
    def withHolderId(holderId: Option[String]): CreditCardOptionalFields = withPublicFields(_.withHolderId(holderId))

    def withoutHolderName: CreditCardOptionalFields = withPublicFields(_.withoutHolderName)
    def withHolderName(holderName: String): CreditCardOptionalFields = withPublicFields(_.withHolderName(holderName))
    def withHolderName(holderName: Option[String]): CreditCardOptionalFields = withPublicFields(_.withHolderName(holderName))

    def withoutBillingAddress: CreditCardOptionalFields = withPublicFields(_.withoutBillingAddress)
    def withBillingAddress(copier: AddressDetailed => AddressDetailed): CreditCardOptionalFields = withPublicFields(_.withBillingAddress(copier))
                         
    def withoutPublicFields: CreditCardOptionalFields = withPublicFields(None)
    def withPublicFields(copier: PublicCreditCardOptionalFields => PublicCreditCardOptionalFields): CreditCardOptionalFields =
      withPublicFields(Some(copier(o.publicFields.getOrElse(somePublicCreditCardOptionalFields))))
    def withPublicFields(publicFields: Option[PublicCreditCardOptionalFields]): CreditCardOptionalFields =
      o.copy(publicFields = publicFields)
  }

  implicit class PublicCreditCardOptionalFieldsTestExtensions(o: PublicCreditCardOptionalFields) {
    def withoutHolderId: PublicCreditCardOptionalFields = withHolderId(None)
    def withHolderId(holderId: String): PublicCreditCardOptionalFields = withHolderId(Some(holderId))
    def withHolderId(holderId: Option[String]): PublicCreditCardOptionalFields = o.copy(holderId = holderId)

    def withoutHolderName: PublicCreditCardOptionalFields = withHolderName(None)
    def withHolderName(holderName: String): PublicCreditCardOptionalFields = withHolderName(Some(holderName))
    def withHolderName(holderName: Option[String]): PublicCreditCardOptionalFields = o.copy(holderName = holderName)

    def withoutBillingAddress: PublicCreditCardOptionalFields = withBillingAddress(None)
    def withBillingAddress(copier: AddressDetailed => AddressDetailed): PublicCreditCardOptionalFields =
      withBillingAddress(Some(copier(o.billingAddressDetailed.getOrElse(someBillingAddress))))
    def withBillingAddress(billingAddress: Option[AddressDetailed]): PublicCreditCardOptionalFields = {
      // Re-calculate cached address fields from AddressDetailed
      PublicCreditCardOptionalFields(
        holderId = o.holderId,
        holderName = o.holderName,
        billingAddressDetailed = billingAddress
      )
    }
  }

  implicit class CustomerTestExtensions(o: Customer) {
    def withoutPhone: Customer = withPhone(None)
    def withPhone(phone: String): Customer = withPhone(Some(phone))
    def withPhone(phone: Option[String]): Customer = o.copy(phone = phone)

    def withoutEmail: Customer = withEmail(None)
    def withEmail(email: String): Customer = withEmail(Some(email))
    def withEmail(email: Option[String]): Customer = o.copy(email = email)

    def withoutName: Customer = withName(None)
    def withName(firstName: String, lastName: String): Customer = withFirstName(firstName).withLastName(lastName)
    def withFirstName(firstName: String): Customer = withName(_.withFirst(firstName))
    def withLastName(lastName: String): Customer = withName(_.withLast(lastName))
    def withName(copier: Name => Name): Customer = withName(Some(copier(o.name.getOrElse(someCustomerName))))
    def withName(name: Option[Name]): Customer = o.copy(name = name)

    def withoutIpAddress: Customer = withIpAddress(None)
    def withIpAddress(ipAddress: String): Customer = withIpAddress(Some(ipAddress))
    def withIpAddress(ipAddress: Option[String]): Customer = o.copy(ipAddress = ipAddress)

    def withoutFax: Customer = withFax(None)
    def withFax(fax: String): Customer = withFax(Some(fax))
    def withFax(fax: Option[String]): Customer = o.copy(fax = fax)

    def withoutCompany: Customer = withCompany(None)
    def withCompany(company: String): Customer = withCompany(Some(company))
    def withCompany(company: Option[String]): Customer = o.copy(company = company)

    def withoutBuyerId: Customer = withBuyerId(None)
    def withBuyerId(buyerId: String): Customer = withBuyerId(Some(buyerId))
    def withBuyerId(buyerId: Option[String]): Customer = o.copy(buyerId = buyerId)

    def withoutClientEnv: Customer = withClientEnv(None)
    def withClientEnv(clientEnv: ClientEnv): Customer = withClientEnv(Some(clientEnv))
    def withClientEnv(clientEnv: Option[ClientEnv]): Customer = o.copy(clientEnv = clientEnv)
  }
  
  implicit class ClientEnvTestExtensions(o: ClientEnv) {
    def withoutUserAgent: ClientEnv = withUserAgent(None)
    def withUserAgent(userAgent: String): ClientEnv = withUserAgent(Some(userAgent))
    def withUserAgent(userAgent: Option[String]): ClientEnv = o.copy(userAgent = userAgent)

    def withoutReferrer: ClientEnv = withReferrer(None)
    def withReferrer(referrer: String): ClientEnv = withReferrer(Some(referrer))
    def withReferrer(referrer: Option[String]): ClientEnv = o.copy(referrer = referrer)

    def withoutDeviceId: ClientEnv = withDeviceId(None)
    def withDeviceId(deviceId: String): ClientEnv = withDeviceId(Some(deviceId))
    def withDeviceId(deviceId: Option[String]): ClientEnv = o.copy(deviceId = deviceId)
  }

  implicit class NameTestExtensions(o: Name) {
    def withFirst(first: String): Name = o.copy(first = first)
    def withLast(last: String): Name = o.copy(last = last)
  }

  implicit class DealTestExtensions(o: Deal) {
    def withId(id: String): Deal = o.copy(id = id)

    def withoutTitle: Deal = withTitle(None)
    def withTitle(title: String): Deal = withTitle(Some(title))
    def withTitle(title: Option[String]): Deal = o.copy(title = title)

    def withoutDescription: Deal = withDescription(None)
    def withDescription(description: String): Deal = withDescription(Some(description))
    def withDescription(description: Option[String]): Deal = o.copy(description = description)

    def withoutInvoiceId: Deal = withInvoiceId(None)
    def withInvoiceId(invoiceId: String): Deal = withInvoiceId(Some(invoiceId))
    def withInvoiceId(invoiceId: Option[String]): Deal = o.copy(invoiceId = invoiceId)

    def withoutShippingAddress: Deal = withShippingAddress(None)
    def withShippingAddress(copier: ShippingAddress => ShippingAddress): Deal =
      withShippingAddress(Some(copier(o.shippingAddress.getOrElse(someShippingAddress))))
    def withShippingAddress(shippingAddress: Option[ShippingAddress]): Deal = o.copy(shippingAddress = shippingAddress)
    
    def withoutItems: Deal = withItems(Seq.empty)
    def withItems(copier: OrderItem => OrderItem): Deal = withItems(o.orderItems.map(copier))
    def withSingleItem(copier: OrderItem => OrderItem): Deal = withItems(Seq(copier(someOrderItem)))
    def withItems(items: Seq[OrderItem]): Deal = o.copy(orderItems = items)

    def withoutTax: Deal = withIncludedCharges(_.withoutTax)
    def withTax(tax: Double): Deal = withIncludedCharges(_.withTax(tax))
    def withTax(tax: Option[Double]): Deal = withIncludedCharges(_.withTax(tax))

    def withoutShippingCost: Deal = withIncludedCharges(_.withoutShipping)
    def withShippingCost(shippingCost: Double): Deal = withIncludedCharges(_.withShipping(shippingCost))
    def withShippingCost(shippingCost: Option[Double]): Deal = withIncludedCharges(_.withShipping(shippingCost))
    
    def withoutIncludedCharges: Deal = withIncludedCharges(None)
    def withIncludedCharges(copier: IncludedCharges => IncludedCharges): Deal = 
      withIncludedCharges(Some(copier(o.includedCharges.getOrElse(someIncludedCharges))))
    def withIncludedCharges(includedCharges: Option[IncludedCharges]): Deal = o.copy(includedCharges = includedCharges)
  }

  implicit class IncludedChargesTestExtensions(o: IncludedCharges) {
    def withoutTax: IncludedCharges = withTax(None)
    def withTax(tax: Double): IncludedCharges = withTax(Some(tax))
    def withTax(tax: Option[Double]): IncludedCharges = o.copy(tax = tax)
    
    def withoutShipping: IncludedCharges = withShipping(None)
    def withShipping(shipping: Double): IncludedCharges = withShipping(Some(shipping))
    def withShipping(shipping: Option[Double]): IncludedCharges = o.copy(shipping = shipping)
  }
  
  implicit class ShippingAddressTestExtensions(o: ShippingAddress) {
    def withoutName: ShippingAddress = withoutFirstName.withoutLastName
    def withName(firstName: String, lastName: String): ShippingAddress = withFirstName(firstName).withLastName(lastName)
    
    def withoutFirstName: ShippingAddress = withFirstName(None)
    def withFirstName(firstName: String): ShippingAddress = withFirstName(Some(firstName))
    def withFirstName(firstName: Option[String]): ShippingAddress = o.copy(firstName = firstName)

    def withoutLastName: ShippingAddress = withLastName(None)
    def withLastName(lastName: String): ShippingAddress = withLastName(Some(lastName))
    def withLastName(lastName: Option[String]): ShippingAddress = o.copy(lastName = lastName)

    def withoutCompany: ShippingAddress = withCompany(None)
    def withCompany(company: String): ShippingAddress = withCompany(Some(company))
    def withCompany(company: Option[String]): ShippingAddress = o.copy(company = company)

    def withoutFax: ShippingAddress = withFax(None)
    def withFax(fax: String): ShippingAddress = withFax(Some(fax))
    def withFax(fax: Option[String]): ShippingAddress = o.copy(fax = fax)

    def withoutPhone: ShippingAddress = withPhone(None)
    def withPhone(phone: String): ShippingAddress = withPhone(Some(phone))
    def withPhone(phone: Option[String]): ShippingAddress = o.copy(phone = phone)

    def withoutEmail: ShippingAddress = withEmail(None)
    def withEmail(email: String): ShippingAddress = withEmail(Some(email))
    def withEmail(email: Option[String]): ShippingAddress = o.copy(email = email)

    def withoutStreet: ShippingAddress = withAddress(_.withoutStreet)
    def withStreet(street: String): ShippingAddress = withAddress(_.withStreet(street))
    def withStreet(street: Option[String]): ShippingAddress = withAddress(_.withStreet(street))

    def withoutCity: ShippingAddress = withAddress(_.withoutCity)
    def withCity(city: String): ShippingAddress = withAddress(_.withCity(city))
    def withCity(city: Option[String]): ShippingAddress = withAddress(_.withCity(city))

    def withoutState: ShippingAddress = withAddress(_.withoutState)
    def withState(state: String): ShippingAddress = withAddress(_.withState(state))
    def withState(state: Option[String]): ShippingAddress = withAddress(_.withState(state))

    def withoutPostalCode: ShippingAddress = withAddress(_.withoutPostalCode)
    def withPostalCode(postalCode: String): ShippingAddress = withAddress(_.withPostalCode(postalCode))
    def withPostalCode(postalCode: Option[String]): ShippingAddress = withAddress(_.withPostalCode(postalCode))

    def withoutCountryCode: ShippingAddress = withAddress(_.withoutCountryCode)
    def withCountryCode(countryCode: Locale): ShippingAddress = withAddress(_.withCountryCode(countryCode))
    def withCountryCode(countryCode: Option[Locale]): ShippingAddress = withAddress(_.withCountryCode(countryCode))

    def withoutAddress: ShippingAddress = withAddress(None)
    def withAddress(copier: AddressDetailed => AddressDetailed): ShippingAddress = 
      withAddress(Some(copier(o.address.getOrElse(someShippingAddressDetailed))))
    def withAddress(address: Option[AddressDetailed]): ShippingAddress = o.copy(address = address)
  }

  implicit class AddressDetailedTestExtensions(o: AddressDetailed) {
    def withoutStreet: AddressDetailed = withStreet(None)
    def withStreet(street: String): AddressDetailed = withStreet(Some(street))
    def withStreet(street: Option[String]): AddressDetailed = o.copy(street = street)

    def withoutCity: AddressDetailed = withCity(None)
    def withCity(city: String): AddressDetailed = withCity(Some(city))
    def withCity(city: Option[String]): AddressDetailed = o.copy(city = city)

    def withoutState: AddressDetailed = withState(None)
    def withState(state: String): AddressDetailed = withState(Some(state))
    def withState(state: Option[String]): AddressDetailed = o.copy(state = state)

    def withoutPostalCode: AddressDetailed = withPostalCode(None)
    def withPostalCode(postalCode: String): AddressDetailed = withPostalCode(Some(postalCode))
    def withPostalCode(postalCode: Option[String]): AddressDetailed = o.copy(postalCode = postalCode)

    def withoutCountryCode: AddressDetailed = withCountryCode(None)
    def withCountryCode(countryCode: Locale): AddressDetailed = withCountryCode(Some(countryCode))
    def withCountryCode(countryCode: Option[Locale]): AddressDetailed = o.copy(countryCode = countryCode)
  }
  
  implicit class OrderItemTestExtensions(o: OrderItem) {
    def withoutId: OrderItem = withId(None)
    def withId(id: String): OrderItem = withId(Some(id))
    def withId(id: Option[String]): OrderItem = o.copy(id = id)
    
    def withoutName: OrderItem = withName(None)
    def withName(name: String): OrderItem = withName(Some(name))
    def withName(name: Option[String]): OrderItem = o.copy(name = name)
    
    def withoutQuantity: OrderItem = withQuantity(None)
    def withQuantity(quantity: Double): OrderItem = withQuantity(Some(quantity))
    def withQuantity(quantity: Option[Double]): OrderItem = o.copy(quantity = quantity)
    
    def withoutPricePerItem: OrderItem = withPricePerItem(None)
    def withPricePerItem(pricePerItem: Double): OrderItem = withPricePerItem(Some(pricePerItem))
    def withPricePerItem(pricePerItem: Option[Double]): OrderItem = o.copy(pricePerItem = pricePerItem)
    
    def withoutDescription: OrderItem = withDescription(None)
    def withDescription(description: String): OrderItem = withDescription(Some(description))
    def withDescription(description: Option[String]): OrderItem = o.copy(description = description)
  }
}

object LibPayTestSupport extends LibPayTestSupport
