app.service('ArrayService', function () {
    return {
        push: function (array, items) {

        },
        unshift: function (array, items) {
            _.each(items, function(item) {
                array.unshift(item);
            })
        }
    }
});