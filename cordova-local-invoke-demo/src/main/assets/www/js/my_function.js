function startActivity() {
    // exec: function(bridgeSecret, service, action, callbackId, argsJson) {
    //     return prompt(argsJson, 'gap:'+JSON.stringify([bridgeSecret, service, action, callbackId]));
    // },
    cordova.exec(function(ret) {
        console.log('success: ' + ret);
    }, function(e) {
        console.log("Error: " + e);
    }, "Activity", "start", []);
}
