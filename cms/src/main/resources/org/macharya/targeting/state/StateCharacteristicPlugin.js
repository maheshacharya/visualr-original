
"use strict";

Ext.namespace('Hippo.Targeting');

Hippo.Targeting.StateCharacteristicPlugin = Ext.extend(Hippo.Targeting.CharacteristicPlugin, {

    constructor: function (config) {
        Hippo.Targeting.StateCharacteristicPlugin.superclass.constructor.call(this, Ext.apply(config, {
            visitorCharacteristic: {
                targetingDataProperty: 'state',
                xtype: 'Hippo.Targeting.TargetingDataPropertyCharacteristic'
            }
        }));
    }

});
