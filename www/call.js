var call = {
  dial: function(phoneNumber, successCallback, errorCallback) {
    cordova.exec(
      successCallback,
      errorCallback,
      'Call',
      'dial',
      [{ "number": phoneNumber }]
    );
  }
}
module.exports = call;
