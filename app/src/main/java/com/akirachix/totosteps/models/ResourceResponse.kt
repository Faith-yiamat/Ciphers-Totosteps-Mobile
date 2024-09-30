package com.akirachix.totosteps.models

import com.akirachix.totosteps.resources

data class ResourceResponse(
    var allResources: List<resources>
){
    private var callback: ((ResourceResponse) -> Unit)? = null
}
