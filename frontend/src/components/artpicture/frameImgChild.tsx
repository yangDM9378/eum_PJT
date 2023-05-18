"use client";

import React, { useEffect, useRef, useState } from "react";
import { Rect, Transformer, Image as KonvaImage, Group } from "react-konva";

type ShapeProps = {
  id: number;
  x: number;
  y: number;
  width: number;
  height: number;
  rotation: number;
  src: CanvasImageSource;
};

interface IconProps {
  shapeProps: ShapeProps;
  isSelected: boolean;
  onSelect: () => void;
  onChange: (newAttrs: ShapeProps) => void;
}

const FrameImgChild = ({
  shapeProps,
  isSelected,
  onSelect,
  onChange,
}: IconProps) => {
  const shapeRef = useRef<any>();
  const trRef = useRef<any>();

  useEffect(() => {
    if (isSelected) {
      trRef.current?.nodes([shapeRef.current]);
      trRef.current?.getLayer().batchDraw();
    } else {
      trRef.current?.nodes([]);
      trRef.current?.getLayer().batchDraw();
    }
  }, [isSelected]);
  const imageRef = useRef<any>(null);

  return (
    <>
      <Group>
        <Rect
          onClick={onSelect}
          onTap={onSelect}
          ref={shapeRef}
          x={shapeProps.x}
          y={shapeProps.y}
          width={shapeProps.width}
          height={shapeProps.height}
          draggable
          rotateEnabled
          onDragEnd={(e) => {
            onChange({
              ...shapeProps,
              x: e.target.x(),
              y: e.target.y(),
            });
          }}
          onTransformEnd={(e) => {
            const node = shapeRef.current;
            const scaleX = node.scaleX();
            const scaleY = node.scaleY();

            node.scaleX(1);
            node.scaleY(1);
            onChange({
              ...shapeProps,
              x: node.x(),
              y: node.y(),
              width: Math.max(5, node.width() * scaleX),
              height: Math.max(5, node.height() * scaleY),
            });
          }}
        />
        <KonvaImage
          ref={imageRef}
          x={shapeProps.x}
          y={shapeProps.y}
          width={shapeProps.width}
          height={shapeProps.height}
          image={shapeProps.src}
          rotateEnabled
          onClick={onSelect}
          onTap={onSelect}
          draggable
          onDragEnd={(e) => {
            onChange({
              ...shapeProps,
              x: e.target.x(),
              y: e.target.y(),
            });
          }}
          onTransformEnd={(e) => {
            const node = shapeRef.current;
            const scaleX = node.scaleX();
            const scaleY = node.scaleY();

            node.scaleX(1);
            node.scaleY(1);
            onChange({
              ...shapeProps,
              x: node.x(),
              y: node.y(),
              width: Math.max(5, node.width() * scaleX),
              height: Math.max(5, node.height() * scaleY),
            });
          }}
        />
      </Group>
      {isSelected && (
        <Transformer
          ref={trRef}
          boundBoxFunc={(oldBox, newBox) => {
            if (newBox.width < 5 || newBox.height < 5) {
              return oldBox;
            }
            return newBox;
          }}
        />
      )}
    </>
  );
};

export default FrameImgChild;
