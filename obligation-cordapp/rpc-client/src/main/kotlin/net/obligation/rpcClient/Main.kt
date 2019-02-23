package net.obligation.rpcClient

import net.corda.client.rpc.CordaRPCClient
import net.corda.client.rpc.CordaRPCClientConfiguration
import net.corda.core.internal.sumByLong
import net.corda.core.messaging.vaultQueryBy
import net.corda.core.utilities.NetworkHostAndPort
import net.corda.finance.contracts.asset.Cash

fun main(args: Array<String>) {
    val parties = hashMapOf("PartyA" to "localhost:10006", "PartyB" to "localhost:10009", "PartyC" to "localhost:10012")
    checkBalance("PartyA", parties["PartyA"]!!)
    checkBalance("PartyB", parties["PartyB"]!!)
    checkBalance("PartyC", parties["PartyC"]!!)
}


fun checkBalance(name: String, networkHostAndPort: String) {
    val nodeAddress = NetworkHostAndPort.parse(networkHostAndPort)
    CordaRPCClient(nodeAddress, CordaRPCClientConfiguration.DEFAULT.copy(minimumServerProtocolVersion = 3)).use("user1", "test") { client ->
        System.out.println("Connected to $name")
        val cashStates = client.proxy.vaultQueryBy<Cash.State>().states
        System.out.println("$name Wonga:")
        cashStates.groupBy { it.state.data.amount.token.product.currencyCode }.forEach {
            System.out.println("${it.key}: ${it.value.sumByLong { value -> value.state.data.amount.quantity }}")
        }
        System.out.println("$name finished...")
    }
}