app.service('ArrayService', function () {
    return {
        popUntil: function (array, length) {
            while (array.length > length) {
                array.pop();
            }
        },
        unshift: function (array, items) {
            _.each(items, function(item) {
                array.unshift(item);
            })
        }
    }
});