"use client";

import React, { useMemo, useState, useEffect } from "react";
import {
  GoogleMap,
  MarkerF,
  MarkerClustererF,
  Autocomplete,
} from "@react-google-maps/api";
import EventOptionModal from "../modals/EventOptionModal";
import MessageModal from "../modals/MessageModal";
import { useAppDispatch, useAppSelector } from "@/redux/hooks";
import { assign, setPinId } from "@/redux/map/mapSlice";
import { Pin } from "@/types/pin";
import GroupPhotoModal from "../modals/GroupPhotoModal";

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

interface Props {
  markerList: Array<Pin> | undefined;
}

function Map({ markerList }: Props) {
  const [curCenter, setCurCenter] = useState({
    lat: 0,
    lng: 0,
  });
  const [mapCenter, setMapCenter] = useState({
    lat: 0,
    lng: 0,
  });

  const [mapref, setMapRef] = useState<google.maps.Map | null>(null);
  const [changeCenter, setChangeCenter] = useState({
    lat: 0,
    lng: 0,
  });
  const dispatch = useAppDispatch();
  const [isOpen, setIsOpen] = useState(false);
  const [messageOpen, setMessageOpen] = useState(false);
  const [messageId, setMessageId] = useState(-1);

  //groupphotomodal 관련 state
  const [isPhotoOpen, setIsPhotoOpen] = useState<boolean>(false);

  // 선택한 사진 인덱스
  const [pidctureId, setPictureId] = useState<number>(0);

  // redux에 넣은 groupphotomodal 인덱스 가져오기
  const pictureId = useAppSelector((state) => state.messageReducer.pictureid);
  console.log(pictureId, "🎈🎈🎈");
  // pictureId에 넣어주기
  useEffect(() => {
    setPictureId(pictureId);
  }, []);

  // 검색기능입니다.
  const [changePlaces, setChangePlaces] =
    useState<google.maps.places.Autocomplete | null>(null);

  const onPlaceChanged = () => {
    if (changePlaces !== null) {
      const place = changePlaces.getPlace();
      if (place.geometry?.location?.lat()) {
        const searchLat = place.geometry.location.lat();
        const searchLng = place.geometry.location.lng();
        setMapCenter({ lat: searchLat, lng: searchLng });
      }
    }
  };
  const onLoad = (autocomplete: google.maps.places.Autocomplete) => {
    setChangePlaces(autocomplete);
  };

  // 함수입니다.
  //지도가 로드되면 매핑합니다.
  const handleOnLoad = (map: google.maps.Map) => {
    setMapRef(map);
  };

  // 지도를 움직일때 지도 중간 지점의 좌표입니다.
  const handleCenterChanged = () => {
    if (mapref) {
      const newCenter = mapref.getCenter();
      if (newCenter) {
        const center = { lat: newCenter.lat(), lng: newCenter.lng() };
        setChangeCenter(center);
      }
    }
  };

  // 메시지 마커 클릭 이벤트입니다.
  const clickMarker = async (pinId: number) => {
    setMessageId(pinId);
    dispatch(setPinId(pinId));
    setMessageOpen(true);
  };

  // const options = {
  //   imagePath:
  //     "https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m1.png", // so you must have m1.png, m2.png, m3.png, m4.png, m5.png and m6.png in that folder
  // };

  // 메시지 추가 이벤트입니다. 버튼을 누르면 모달이 열립니다.
  const addLetter = () => {
    // alert(`추가버튼을 누르셨습니다. ${changeCenter.lat} ${changeCenter.lng}`);
    const addCoords = {
      lat: changeCenter.lat,
      lng: changeCenter.lng,
    };
    dispatch(assign(addCoords));
    setIsOpen(true);
  };

  const getUserGps = () => {
    navigator.geolocation.getCurrentPosition(function (position) {
      if (position) {
        const lat = position.coords.latitude;
        const lng = position.coords.longitude;
        setCurCenter({ lat, lng });
        setMapCenter({ lat, lng });
      }
    });
  };

  useEffect(() => {
    getUserGps();
  }, []);

  return (
    <section className="h-[75%] relative flex justify-center items-center">
      <GoogleMap
        onLoad={handleOnLoad}
        center={mapCenter}
        zoom={16}
        mapContainerStyle={{ width: "90%", height: "90%" }}
        options={GoogleMapOptions}
        onCenterChanged={handleCenterChanged}
      >
        <Autocomplete onPlaceChanged={onPlaceChanged} onLoad={onLoad}>
          <input
            type="text"
            // onClick={(e) => {
            //   e.currentTarget.value = "";
            // }}
            style={{
              boxSizing: `border-box`,
              border: `1px solid transparent`,
              width: `240px`,
              height: `32px`,
              padding: `0 12px`,
              borderRadius: `3px`,
              boxShadow: `0 2px 6px rgba(0, 0, 0, 0.3)`,
              fontSize: `14px`,
              outline: `none`,
              textOverflow: `ellipses`,
              position: "absolute",
              left: "50%",
              marginLeft: "-120px",
              top: "2%",
            }}
          />
        </Autocomplete>
        {/* 지도의 센터 좌표 이미지입니다. */}
        <img
          className="absolute top-[50%] left-[50%] w-[5vh]"
          style={{ transform: "translate(-50%, -50%)" }}
          src="/map/centerTarget.png"
          alt="center"
        />
        {/* 좌표 클러스터링 */}
        <MarkerClustererF minimumClusterSize={3}>
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
        </MarkerClustererF>
        {/* 지도에 메시지 추가하기. */}
        <img
          className="absolute top-[90%] left-[89%] w-[10vh]"
          style={{ transform: "translate(-50%, -50%)" }}
          src="/map/plus.png"
          alt="center"
          onClick={addLetter}
        />
        <img
          src="/map/curPosition.png"
          alt="curPosition"
          className="absolute top-[78%] left-[89%] w-[7vh]"
          style={{ transform: "translate(-50%, -50%)" }}
          onClick={getUserGps}
        />
      </GoogleMap>
      <EventOptionModal
        isOpen={isOpen}
        setIsOpen={setIsOpen}
        changeCenter={changeCenter}
      />
      <MessageModal
        messageOpen={messageOpen}
        setMessageOpen={setMessageOpen}
        messageId={messageId}
        setIsPhotoOpen={setIsPhotoOpen}
      />
      <GroupPhotoModal
        isOpen={isPhotoOpen}
        setIsOpen={setIsPhotoOpen}
        pictureId={pidctureId}
      />
    </section>
  );
}

export default React.memo(Map);
