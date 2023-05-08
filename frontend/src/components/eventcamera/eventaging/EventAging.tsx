// "use client";
import { useAppSelector } from "@/redux/hooks";
import { agingEventApi } from "@/services/eventApi";
import { AgingEventResult } from "@/types/event";
import { useMutation } from "@tanstack/react-query";
import Konva from "konva";
import React, { useEffect, useRef, useState } from "react";
import { Stage, Layer, Image, Transformer, Group, Rect } from "react-konva";

// const EventAging = () => {
//   const pictureImg = useAppSelector((state) => state.eventReducer.pictureimg);
//   const agingImg = useAppSelector((state) => state.eventReducer.eventimageurl);
//   const groupId = useAppSelector((state) => state.coordsReducer.groupId);
//   const pinId = useAppSelector((state) => state.coordsReducer.pinId);
//   const [decorativeImageProps, setDecorativeImageProps] = useState({
//     x: 10,
//     y: 0,
//     width: 150,
//     height: 150,
//     rotation: 0,
//   });
//   const [selected, setSelected] = useState(false);
//   const [decorativeImage, setDecorativeImage] =
//     useState<HTMLImageElement | null>(null);
//   const [backgroundImage, setBackgroundImage] = useState<
//     HTMLImageElement | undefined
//   >(undefined);

//   const stageRef = useRef<Konva.Stage>(null);

//   useEffect(() => {
//     const img = new window.Image();
//     img.src = pictureImg;
//     img.onload = () => {
//       setBackgroundImage(img);
//     };
//   }, []);

//   useEffect(() => {
//     const img = new window.Image();
//     // img.crossOrigin = "anonymous";
//     img.src = `${process.env.NEXT_PUBLIC_IMAGE_URL}${agingImg}`;
//     img.onload = () => {
//       setDecorativeImage(img);
//     };
//   }, []);

//   // 사진 저장 API 통신
//   const agingEventMutation = useMutation(agingEventApi, {
//     onSuccess: (data) => {
//       setResponse(data);
//       // queryClient로 바로 적용되게 사진들
//       // queryClient.invalidateQueries({ queryKey: [""] });
//     },
//   });

//   // 사진 저장
//   // aging결과
//   const [response, setResponse] = useState<AgingEventResult | null>(null);
//   const handleSave = async () => {
//     const stage = stageRef.current;
//     if (stage) {
//       // 캔버스 이미지를 data URL로 변환합니다.
//       const dataURL = stage.toDataURL();
//       const jsonReq = { groupId: groupId, pinId: pinId };
//       const blobRes = await (await fetch(dataURL)).blob();
//       const formData = new FormData();
//       formData.append("image", blobRes, "image.png");
//       formData.append(
//         "pictureAddReq",
//         new Blob([JSON.stringify(jsonReq)], { type: "application/json" })
//       );
//       agingEventMutation.mutate(formData);
//     }
//   };

//   return (
//     <Stage width={300} height={300} ref={stageRef}>
//       {/* Layer는 캔버스 내에서 요소들을 그룹화하기 위한 것입니다. */}
//       {/* 배경 이미지 */}
//       <Layer>
//         <Image image={backgroundImage} width={300} height={300} />
//         {/* 꾸밀 이미지 */}
//         {decorativeImage && (
//   <Group>
//     <Image
//       image={decorativeImage}
//       {...decorativeImageProps}
//       draggable
//       onClick={() => {
//         console.log(111);
//         setSelected(true);
//       }}
//       onDragStart={() => {
//         setSelected(false);
//       }}
//       onTransformEnd={(event) =>
//         setDecorativeImageProps(event.currentTarget.attrs)
//       }
//     />
//     // Transformer는 꾸밀 이미지에만 적용되어야 합니다.
//     {decorativeImage && selected && (
//       <Transformer
//         rotateEnabled={true}
//         resizeEnabled={true}
//         rotateAnchorOffset={20}
//         anchorSize={10}
//         borderDash={[3, 3]}
//         {...decorativeImageProps}
//         onTransformEnd={(event) =>
//           setDecorativeImageProps(event.currentTarget.attrs)
//         }
//       />
//     )}
//   </Group>
// )}
//       </Layer>
//     </Stage>
//   );
// };

// export default EventAging;

const EventAging = () => {
  const [rectangles, setRectangles] = useState([]);

  const handleClick = () => {
    const rect = {
      x: Math.random() * window.innerWidth,
      y: Math.random() * window.innerHeight,
      width: 50,
      height: 50,
      fill: "red",
    };
    setRectangles([...rectangles, rect]);
  };

  return (
    <Stage width={window.innerWidth} height={window.innerHeight}>
      <Layer>
        {rectangles.map((rect, index) => (
          <Rect key={index} {...rect} draggable />
        ))}
      </Layer>
      <Layer>
        <Rect x={100} y={100} width={100} height={100} fill="green" draggable />
      </Layer>
    </Stage>
  );
};

export default EventAging;
