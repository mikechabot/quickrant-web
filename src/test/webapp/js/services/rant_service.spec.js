describe('rant_service.js', function() {

    var service = undefined;

    var log = {};
    var constants = {};
    var data = {};
    var dataAccessService = {};
    var dialogService = {
        open: function () {

        }
    };

    beforeEach(function() {
        module('mocks');
        module('test', function($provide) {
            $provide.value('$log', log);
            $provide.value('QR_CONST', constants);
            $provide.value('QR_DATA', data);
            $provide.value('DataAccessService', dataAccessService);
            $provide.value('DialogService', dialogService);
        });
    });

    beforeEach(inject(function(RantService) {
        service = RantService;
    }));

    describe('getPaginatedRants(pageNumber)', function() {

        dataAccessService.get = jasmine.createSpy('dataAccessService get() spy').and.returnValue(promiseUtil.getPromise());

        it('should call the correct rest url', function () {
            service.getPaginatedRants(123);
            expect(dataAccessService.get).toHaveBeenCalledWith('/rants/page/123');
        });

    });

    describe('postRant(rant)', function() {

        dataAccessService.post = jasmine.createSpy('dataAccessService post() spy').and.returnValue(promiseUtil.getPromise());

        it('should call the correct rest url', function () {
            var rant = testData.getTestRant(rant);
            service.postRant(rant);
            expect(dataAccessService.post).toHaveBeenCalledWith('/rants', rant);
        });
        it('should throw exception if the rant is null', function () {
            expect(function() {
                service.postRant(null);
            }).toThrow(new Error('Rant cannot be null'));
        });
        it('should throw exception if the rant is undefined', function () {
            expect(function() {
                service.postRant(undefined);
            }).toThrow(new Error('Rant cannot be null'));
        });

    });



});
