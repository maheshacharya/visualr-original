var layer = "world-countries";
var server = "http://localhost:8080/";
var countryNameRestQuery = server + "cms/ws/geojson/valuelist?name={valuelist}&filterLabels=";
var url = server + "cms/ws/geojson/layer?mapLayerName=";
var tgtchars = server + "cms/ws/geojson/valuelist?name=geo-targeting-characteristics";
var geoLayers = server + "cms/ws/geojson/valuelist?name=geo-layers";
var saveQuery = server + "cms/ws/geojson/save";
var layerPropertiesQuery = server + "cms/ws/geojson/layerProperties?mapLayerName=";

var tid = setTimeout(initMap, 2000);

function abortTimer() { // to be called when you want to stop the timer
    clearTimeout(tid);
}
function initMap() {
    if ($("#map").is(":visible")) {
        clearTimeout(tid);
    } else {
        loadMapLayer(url + layer);
    }
}


$(document).ready(function () {
    $(".hippo-perspective-visualrelevanceperspective").on('click', function () {

        loadMapLayer(url + layer);
    });

    loadMapLayer(url + layer);
    console.log(url + layer);
    $(".options").on("change", function () {
        layer = $(".options").val();
        if (layer === '') {
            layer = "world-countries";
        }
        var u = url + layer;
        console.log(u);
        loadMapLayer(u);
    });

    $("#map").focus(function () {
        console.log("map in foucs");
    });

    $(".dialog-close-button").on('click', function () {

        $(".dialog-main").slideUp();
        $(".target-fields").slideUp();
        $(this).hide();
        $(".info").removeClass("opened");
        $(".info").addClass("minimized");
        $(".dialog-open-button").show();

    });
    $(".dialog-open-button").on('click', function () {

        $(".dialog-main").slideDown();
        $(".target-fields").slideDown();
        $(this).hide();
        $(".info").removeClass("minimized");
        $(".info").addClass("opened");
        $(".dialog-close-button").show();

    });
});

var layerData;


function loadMapLayer(mapLayerDataSource) {


    getData(layerPropertiesQuery + $(".options").val(), processLayerData);
}

function loadMapLayerx(mapLayerDataSource) {

    if (mapLayerDataSource === undefined) {
        mapLayerDataSource = url + layer;
    }
    // getData(layerPropertiesQuery + mapLayerDataSource, processLayerData);


    var vectorSource = new ol.source.Vector({
        url: mapLayerDataSource,
        format: new ol.format.GeoJSON()
    });
    var lab = document.getElementById('label-container');
    lab.innerHTML = "";

    $("#map").html("");
    var map = new ol.Map({
        layers: [
            new ol.layer.Tile({
                source: new ol.source.OSM()
            }),
            new ol.layer.Vector({
                source: vectorSource
            })
        ],
        renderer: 'canvas',
        target: 'map',
        view: new ol.View({
            center: [0, 0],
            zoom: 2

        })
    });

    map.minZoomLevel = 2;

    // if (layer == 'world-countries') {
    //   map.getView().setZoom(2);
    //} else {
    // map.getView().setCenter(ol.proj.transform([-99.9018, 41.4925], 'EPSG:4326', 'EPSG:3857'));
    var long = parseInt(layerData.longitude);
    var lat = parseInt(layerData.latitude);

    map.getView().setCenter(ol.proj.transform([long, lat], 'EPSG:4326', 'EPSG:3857'));
    map.getView().setZoom(layerData.zoomLevel);
    console.log("data " + layerData.zoomLevel + " " + layerData.longitude);
    //}

    // map.setCenter(OpenLayers.LonLat(42.3601, 71.0589), 5);
// a normal select interaction to handle click
    var select = new ol.interaction.Select();
    map.addInteraction(select);

    var selectedFeatures = select.getFeatures();

// a DragBox interaction used to select features by drawing boxes
    var dragBox = new ol.interaction.DragBox({
        condition: ol.events.condition.platformModifierKeyOnly
    });

    map.addInteraction(dragBox);

    //var infoBox = document.getElementById('info');

    dragBox.on('boxend', function () {
        // features that intersect the box are added to the collection of
        // selected features, and their names are displayed in the "info"
        // div
        var info = [];
        var extent = dragBox.getGeometry().getExtent();
        vectorSource.forEachFeatureIntersectingExtent(extent, function (feature) {
            selectedFeatures.push(feature);
            // info.push(feature.get('name'));
            var layer1 = $(".options").val();
            var value = getPropertyValue(feature, "name");
            info.push(value);

        });

        if (info.length > 0) {
            var label = $(".info");
            $(".dialog-main").html(info.join(', '));
            $(label).slideDown();
            $(".target-values").val($(".dialog-main").text());
            $(".target-fields").show();
            getData(countryNameRestQuery + "" + $(".dialog-main").text(), handleCountryCodes);

        }
    });

// clear selection when drawing a new box and when clicking on the map
    dragBox.on('boxstart', function () {
        selectedFeatures.clear();
        // infoBox.innerHTML = '&nbsp;';
    });
    map.on('click', function (e) {
        // selectedFeatures.clear();

        map.forEachFeatureAtPixel(e.pixel, function (feature, layer) {

            var label = $(".info");
            var layer1 = $(".options").val();
            var value = getPropertyValue(feature, "name");
            if (value == '') {
                //  value = layer1;
            }
            $(".dialog-main").html(value);
            $(".selected-values").val(value);
            $(".target-values").val($(".dialog-main").text());
            getData(countryNameRestQuery + "" + $(".dialog-main").text(), handleCountryCodes);
            $(label).css("z-index", 9999999);
            $(label).show();
            $(".target-fields").show();

        });

    });


}

function processLayerData(data) {
    layerData = data;
    loadMapLayerx(url + layer);
    $(".tgtchars").val(layerData.defaultCharacteristic);

}

function getPropertyValue(feature, name) {
    if (feature.get(name) == undefined) {
        return feature.get(name.toUpperCase());
    } else {
        return feature.get(name)

    }
}

function processQuery(query) {
    if (query.indexOf("cms/ws/geojson/valuelist") >= 0) {
        return query.replace("{valuelist}", $(".options").val());
    } else {
        return query;
    }
}

function handleCountryCodes(data) {
    // console.log(data);
    var val = "";
    $(data.countries).each(function (i, country) {
        if (i > 0) {
            val += ",";
        }
        val += country.code;
    });
    if (val !== '') {
        $(".target-values").val(val);
    }
}
function getData(query, callback) {
    var processedQuery = processQuery(query);
    console.log("final query: " + processedQuery);
    $.ajax({
        dataType: "json",
        url: processedQuery,
        type: "GET",
        success: callback
    })
}


function runPost(query, callback, data) {
    var processedQuery = processQuery(query);
    console.log("final query: " + processedQuery);
    $.ajax({
        dataType: "json",
        url: processedQuery,
        data: data,
        type: "POST",
        success: callback
    })
}

var app = angular.module('vrApp', []);


app.controller('vrCtrl', function ($scope, $http) {
    $scope.formdata = {};
    $http.get(tgtchars).
        success(function (data, status, headers, config) {


            $scope.tgtchars = data.valuelist;
            $scope.tgtchar = data.valuelist[0].key;


        }).
        error(function (data, status, headers, config) {
            // log error
        });

    $http.get(geoLayers).
        success(function (data, status, headers, config) {

            $scope.geoLayers = data.valuelist;
            $scope.geolayers = data.valuelist[0].key;


        }).
        error(function (data, status, headers, config) {
            // log error
        });

    $scope.submit = function () {

        var data = $(".form-chars").serialize();
        runPost(saveQuery, save, data);

    };

    function save(data) {
        //  alert(data);
        if (data.message == 'success') {
            showMessage("A new target group is saved.");
        }
    }

    function showMessage(message) {
        $(".form-message").html(message).fadeIn();
        $(".form-message").delay(3200).fadeOut(300);
    }

});




