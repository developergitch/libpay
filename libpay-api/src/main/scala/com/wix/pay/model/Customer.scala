/*      __ __ _____  __                                              *\
**     / // // /_/ |/ /          Wix                                 **
**    / // // / /|   /           (c) 2006-2014, Wix LTD.             **
**   / // // / //   |            http://www.wix.com/                 **
**   \__/|__/_//_/| |                                                **
\*                |/                                                 */
package com.wix.pay.model


/** Represents a customer. */
case class Customer(name: Option[Name] = None,
                    phone: Option[String] = None,
                    email: Option[String] = None,
                    ipAddress: Option[String] = None,
                    fax: Option[String] = None,
                    company: Option[String] = None,
                    buyerId: Option[String] = None,
                    clientEnv: Option[ClientEnv] = None) {
  def firstName : Option[String] = {
    name map (_.first)
  }
  def lastName : Option[String] = {
    name map (_.last)
  }
}

case class Name(first: String, last: String)
