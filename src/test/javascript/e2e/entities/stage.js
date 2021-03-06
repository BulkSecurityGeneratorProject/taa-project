'use strict';

describe('Stage e2e test', function () {

    var username = element(by.id('username'));
    var password = element(by.id('password'));
    var entityMenu = element(by.id('entity-menu'));
    var accountMenu = element(by.id('account-menu'));
    var login = element(by.id('login'));
    var logout = element(by.id('logout'));

    beforeAll(function () {
        browser.get('/');

        username.sendKeys('admin');
        password.sendKeys('admin');
        element(by.css('button[type=submit]')).click();
    });

    it('should load Stages', function () {
        entityMenu.click();
        element.all(by.css('[ui-sref="stage"]')).first().click().then(function () {
            expect(element.all(by.css('h2')).first().getText()).toMatch(/Stages/);
        });
    });

    it('should load create Stage dialog', function () {
        element(by.css('[ui-sref="stage.new"]')).click().then(function () {
            expect(element(by.css('h4.modal-title')).getText()).toMatch(/Créer ou éditer un stage/);
            element(by.css('button.close')).click();
        });
    });

    afterAll(function () {
        accountMenu.click();
        logout.click();
    });
});
