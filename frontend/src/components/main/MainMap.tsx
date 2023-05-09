"use client";

import React, { useMemo, useState, useEffect } from "react";
import {
  GoogleMap,
  MarkerF,
  MarkerClustererF,
  Autocomplete,
} from "@react-google-maps/api";

// 지도 옵션입니다.
const GoogleMapOptions: google.maps.MapOptions = {
  tilt: 0,
  zoomControl: false,
  minZoom: 3,
  maxZoom: 19,
  streetViewControl: false,
  disableDefaultUI: true,
  gestureHandling: "greedy",
  styles: [
    {
      featureType: "poi",
      elementType: "all",
      stylers: [{ visibility: "off" }],
    },
  ],
};

const MainMap = () => {
  // 지도 상태 값
  const [mapCenter, setMapCenter] = useState({
    lat: 0,
    lng: 0,
  });

  //지도가 로드되면 매핑합니다.
  const [mapref, setMapRef] = useState<google.maps.Map | null>(null);
  const handleOnLoad = (map: google.maps.Map) => {
    setMapRef(map);
  };

  // 현재 위치를 가져옵니다.
  const getUserGps = () => {
    navigator.geolocation.getCurrentPosition(function (position) {
      if (position) {
        const lat = position.coords.latitude;
        const lng = position.coords.longitude;
        setMapCenter({ lat, lng });
      }
    });
  };

  useEffect(() => {
    getUserGps();
  }, []);

  return (
    <section className="h-[100%] relative flex justify-center items-center">
      <GoogleMap
        onLoad={handleOnLoad}
        center={mapCenter}
        zoom={8}
        mapContainerStyle={{ width: "80%", height: "90%" }}
        options={GoogleMapOptions}
      >
        {/* <MarkerF position={{ lat: mapCenter.lat, lng: mapCenter.lng }} /> */}
        {/* 좌표 클러스터링 */}
        {/* <MarkerClustererF minimumClusterSize={3}>
          {(clusterer) => (
            <>
              {markerList &&
                markerList.map((marker) => (
                  <MarkerF
                    key={marker.pinId}
                    position={{ lat: marker.latitude, lng: marker.longitude }}
                    clusterer={clusterer}
                    onClick={() => clickMarker(marker.pinId)}
                  />
                ))}
            </>
          )}
        </MarkerClustererF> */}

        {/* <img
          src="/map/curPosition.png"
          alt="curPosition"
          className="absolute top-[90%]  left-[10%] w-[5vh]"
          style={{ transform: "translate(-50%, -50%)" }}
          onClick={getUserGps}
        /> */}
      </GoogleMap>
    </section>
  );
};

export default React.memo(MainMap);
