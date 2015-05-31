describe('common.js', function() {

    var foo = 'foo';
    var bar = 'bar';
    var t = true;
    var two = 2;
    var array = [foo, t, two];

    var foobar = function() {
        return foo+bar;
    };

    var object = {
        foo: foo,
        bar: bar,
        foobar: foobar,
        t: t,
        two: two,
        array: array,
        object: { foo:foo, t:t, two:two, array:array, foobar:foobar }
    };

    describe('hasValue(object)', function() {

        it("should return false if passed null", function() {
            expect(hasValue(null)).toEqual(false);
        });
        it("should return false if passed undefined", function() {
            expect(hasValue(undefined)).toEqual(false);
        });
        it("should return true passed an empty String", function() {
            expect(hasValue('')).toEqual(true);
        });
        it("should return true passed an empty array", function() {
            expect(hasValue([])).toEqual(true);
        });
        it("should return true passed an empty object", function() {
            expect(hasValue({})).toEqual(true);
        });
        it("should return true if passed a boolean (false)", function() {
            expect(hasValue(false)).toEqual(true);
        });
        it("should return true if passed a boolean (true)", function() {
            expect(hasValue(true)).toEqual(true);
        });

    });

    describe('isEmpty(object)', function() {

        it("should return true if passed null", function() {
            expect(isEmpty(null)).toBe(true);
        });
        it("should return true if passed undefined", function() {
            expect(isEmpty(undefined)).toBe(true);
        });
        it("should return true if passed an empty String", function() {
            expect(isEmpty('')).toBe(true);
        });
        it("should return true if passed an empty array", function() {
            expect(isEmpty([])).toBe(true);
        });
        it("should return true if passed an empty object", function() {
            expect(isEmpty({})).toBe(true);
        });
        it("should return true if passed a boolean (false)", function() {
            expect(isEmpty(false)).toBe(true);
        });
        it("should return false if passed a boolean (true)", function() {
            expect(isEmpty(true)).toBe(false);
        });

    });



    describe('copyObject(copyFrom, copyTo)', function() {

        describe('copyObject shouldn\'t overwrite primitives', function() {

            var destination;

            beforeEach(function() {
                destination = undefined;
            });

            it("should not overwrite the destination with a String literal", function() {
                destination = copyObject(foo, destination);
                expect(destination).not.toEqual(foo);
            });
            it("should not overwrite the destination with a boolean literal", function() {
                copyObject(true, destination);
                expect(destination).not.toBe(true);
            });
            it("should not overwrite the destination with an Number literal", function() {
                copyObject(two, destination);
                expect(destination).not.toEqual(two);
            });

            afterEach(function() {
                expect(destination).toBeUndefined();
            });

        });

        describe('copyObject should copy properties from one composite object to another ', function() {

            var destination;

            beforeEach(function() {
                destination = object;
            });

            it("should overwrite a object property if it already exists", function() {
                var newFoo = { foo: array };
                copyObject(newFoo, destination);
                expect(destination.foo).toEqual(newFoo.foo);
            });

            it("should add an object property if it doesn\'t exist", function() {
                var baz = { baz: 'baz' };
                copyObject(baz, destination);
                expect(destination.baz).toEqual(baz.baz);
            });

            it("shouldn\'t copy properties if the source object is undefined", function() {
                copyObject(undefined, destination);
                expect(destination).toEqual(object);
            });
            it("shouldn\'t copy properties if the source object is null", function() {
                copyObject(null, destination);
                expect(destination).toEqual(object);
            });

            it("should copy properties if the destination is undefined", function() {
                destination = undefined;
                destination = copyObject(object, destination);
                expect(destination).toEqual(object);
            });

            it("should properties if the destination object is null", function() {
                destination = null;
                destination = copyObject(object, destination);
                expect(destination).toEqual(object);
            });

        });

    });

});