function subtract(val1, val2) {
  if (!angular.isNumber(val1) || !angular.isNumber(val2)) return;
  return val1 - val2;
}

function getDefaultString(object, def) {
  if (!angular.isString(object) || !angular.isDefined(object) || object.length == 0) return def;
  return object;
}