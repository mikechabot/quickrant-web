describe('rant_service.js', function() {

    var service = undefined;

    var log = {};
    var QR_CONST = {};
    var QR_DATA = {};
    var dataAccessService = {};
    var dialogService = {};

    beforeEach(function() {
        module('test', function($provide) {
            $provide.value('$log', log);
            $provide.value('QR_CONST', QR_CONST);
            $provide.value('QR_DATA', QR_DATA);
            $provide.value('DataAccessService', dataAccessService);
            $provide.value('DialogService', dialogService);
        });
    });

    beforeEach(inject(function(RantService) {
        service = RantService;
    }));

    describe('getPageByPageNumber(pageNumber)', function() {

        beforeEach(function() {
            dataAccessService.get = jasmine.createSpy('dataAccessService get() spy').and.returnValue(promiseUtil.getPromise());
        });

        it('should call the correct rest url', function () {
            service.getPageByPageNumber(123);
            expect(dataAccessService.get).toHaveBeenCalledWith('/rants/page/123');
        });

        afterEach(function() {
            dataAccessService = {};
        });

    });

    describe('postRant(rant, selection)', function() {

        var rant;
        var selection;

        beforeEach(function() {
            dataAccessService.post = jasmine.createSpy('dataAccessService post() spy').and.returnValue(promiseUtil.getPromise());
            rant = testData.getTestRant();
            selection = testData.getTestSelection();
        });

        it('should call the correct rest url', function () {
            var newRant = service.createNewRant(rant, selection);
            service.postRant(rant, selection);
            expect(dataAccessService.post).toHaveBeenCalledWith('/rants', newRant);
        });
        it('should throw exception if the rant is null', function () {
            expect(function() {
                service.postRant(null, selection);
            }).toThrow(new Error('Rant and/or selection cannot be null'));
        });
        it('should throw exception if the rant is undefined', function () {
            expect(function() {
                service.postRant(undefined, selection);
            }).toThrow(new Error('Rant and/or selection cannot be null'));
        });
        it('should throw exception if the selection is null', function () {
            expect(function() {
                service.postRant(rant, null);
            }).toThrow(new Error('Rant and/or selection cannot be null'));
        });
        it('should throw exception if the selection is undefined', function () {
            expect(function() {
                service.postRant(rant, undefined);
            }).toThrow(new Error('Rant and/or selection cannot be null'));
        });

        afterEach(function() {
            dataAccessService = {};
        });

    });

});
