"use client";

import React, { useMemo } from "react";

import { GoogleMap, MarkerF, MarkerClustererF } from "@react-google-maps/api";

const GoogleMapOptions: google.maps.MapOptions = {
  tilt: 0,
  zoomControl: false,
  streetViewControl: false,
  disableDefaultUI: true,
  gestureHandling: "greedy",
};

function Map() {
  // const handleOnLoad = (map: google.maps.Map) => {
  //   const bounds = new google.maps.LatLngBounds();
  //   markers.forEach(({ position }) => bounds.extend(position));
  //   map.fitBounds(bounds);
  // };

  const clickMarker = () => {
    console.log("hi");
  };

  const options = {
    imagePath:
      "https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m1.png", // so you must have m1.png, m2.png, m3.png, m4.png, m5.png and m6.png in that folder
  };

  const mapCenter = useMemo(() => ({ lat: 35.205331, lng: 126.811123 }), []);

  return (
    <section className="h-[75%] flex justify-center items-center">
      <GoogleMap
        // onLoad={handleOnLoad}
        center={mapCenter}
        zoom={14}
        mapContainerStyle={{ width: "90%", height: "90%" }}
        options={GoogleMapOptions}
      >
        {/* <MarkerF position={{ lat: 53, lng: 9 }} /> */}
        <MarkerClustererF>
          {(clusterer) => (
            <>
              {markers.map((marker) => (
                <MarkerF
                  key={marker.id}
                  position={marker.position}
                  clusterer={clusterer}
                  onClick={clickMarker}
                />
              ))}
            </>
          )}
        </MarkerClustererF>
      </GoogleMap>
    </section>
  );
}

export default React.memo(Map);

const markers = [
  {
    id: 1,
    position: { lat: 35.203125331, lng: 126.8421111252 },
  },
  {
    id: 2,
    position: { lat: 35.220115123313, lng: 126.811114311 },
  },
  {
    id: 3,
    position: { lat: 35.2252412335, lng: 126.8111235 },
  },
  {
    id: 4,
    position: { lat: 35.1205436, lng: 126.8111236 },
  },
  {
    id: 5,
    position: { lat: 35.112320562, lng: 126.8115231 },
  },
  {
    id: 6,
    position: { lat: 35.25553314, lng: 126.814123212312 },
  },
  {
    id: 7,
    position: { lat: 35.221205331, lng: 126.814411233 },
  },
  {
    id: 8,
    position: { lat: 35.205331, lng: 126.8111231 },
  },
  {
    id: 9,
    position: { lat: 53.876, lng: 9.17 },
  },
  {
    id: 10,
    position: { lat: 53.345, lng: 9.23 },
  },
  {
    id: 11,
    position: { lat: 53.276, lng: 9.34 },
  },
];
