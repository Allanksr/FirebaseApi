package allanksr.com.firebase.cloudFunctions

import com.google.firebase.functions.FirebaseFunctions

interface FunctionsPresenter {
  fun functionsInstance(): FirebaseFunctions
  fun singleCall()
  fun callWithData(coinType: String)
}