/**
 * Custom filter to trust in
 */
app.filter('trusted', ['$sce', function($sce) {
    return function(input) {
       return $sce.trustAsHtml(input);
    }
}]);

/**
 * Determine whether an object is null/undefined. In the
 * event of a String, ensure the length > 0.
 *
 * @param object
 * @returns {boolean}
 */
function hasValue(object) {
    if (object !== null && angular.isDefined(object)) {
        if (angular.isString(object)) {
            return !_.isEmpty(object);
        }
        return true;
    } else {
        return false;
    }
}

/**
 * Copy the properties of one object onto another
 * @param original
 * @param copy
 * @returns {*}
 */
function copyObject(copyFrom, copyTo) {
    for (var key in copyFrom) {
        copyTo[key] = copyFrom[key];
    }
    return copyTo;
}