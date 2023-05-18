"use client";

import React, { useEffect, useRef } from "react";
import { Rect, Transformer, Image as KonvaImage, Group } from "react-konva";

type ShapeProps = {
  id: number;
  x: number;
  y: number;
  width: number;
  height: number;
  rotation: number;
  src: CanvasImageSource;
  title: number;
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
  const groupRef = useRef<any>();
  const imageRef = useRef<any>(null);
  const trRef = useRef<any>();

  const handleDragMove = (e: any) => {
    const node = groupRef.current;
    const newX = e.target.x();
    const newY = e.target.y();
    const newRotation = e.target.rotation();

    node.position({ x: newX, y: newY, rotation: newRotation });
    onChange({
      ...shapeProps,
      x: newX,
      y: newY,
      rotation: newRotation,
    });
  };

  const handleTransformEnd = (e: any) => {
    const shapeNode = shapeRef.current;
    const imageNode = imageRef.current;
    const scaleX = imageNode.scaleX();
    const scaleY = imageNode.scaleY();
    onChange({
      ...shapeProps,
      width: Math.max(5, imageNode.width() * scaleX),
      height: Math.max(5, imageNode.height() * scaleY),
    });
  };

  useEffect(() => {
    if (isSelected) {
      trRef.current?.nodes([shapeRef.current]);
      trRef.current?.getLayer().batchDraw();
    } else {
      trRef.current?.nodes([]);
      trRef.current?.getLayer().batchDraw();
    }
  }, [isSelected]);

  return (
    <Group
      ref={groupRef}
      draggable
      onDragMove={handleDragMove}
      onTransformEnd={handleTransformEnd}
    >
      <Rect
        onClick={onSelect}
        onTap={onSelect}
        ref={shapeRef}
        x={shapeProps.x}
        y={shapeProps.y}
        width={shapeProps.width}
        height={shapeProps.height}
        rotation={shapeProps.rotation}
        onTransformEnd={() => {
          const node = shapeRef.current;
          const scaleX = node.scaleX();
          const scaleY = node.scaleY();

          node.scaleX(1);
          node.scaleY(1);

          onChange({
            ...shapeProps,
            x: node.x(),
            y: node.y(),
            rotation: node.rotation(),
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
        rotation={shapeProps.rotation}
        image={shapeProps.src}
        onClick={onSelect}
        onTap={onSelect}
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
            rotation: e.target.rotation(),
            width: Math.max(5, node.width() * scaleX),
            height: Math.max(5, node.height() * scaleY),
          });
        }}
      />

      {/* 선택된 아이콘만 Transformer 컴포넌트를 사용할 수 있음*/}
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
    </Group>
  );
};

export default FrameImgChild;
