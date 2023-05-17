"use client";

import React, { useEffect, useRef, useState } from "react";
import { Rect, Transformer, Image as KonvaImage, Group } from "react-konva";

type ShapeProps = {
  id: number;
  x: number;
  y: number;
  width: number;
  height: number;
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
  const groupRef = useRef<any>();
  const trRef = useRef<any>();

  const handleDragMove = (e: any) => {
    const node = groupRef.current;
    const newX = e.target.x();
    const newY = e.target.y();

    node.position({ x: newX, y: newY });
    onChange({
      ...shapeProps,
      x: newX,
      y: newY,
    });
  };

  const handleTransformEnd = (e: any) => {
    const node = imageRef.current;
    const scaleX = node.scaleX();
    const scaleY = node.scaleY();

    node.scaleX(1);
    node.scaleY(1);
    node.rotation(e.target.rotation());
    onChange({
      ...shapeProps,
      x: node.x(),
      y: node.y(),
      width: Math.max(5, node.width() * scaleX),
      height: Math.max(5, node.height() * scaleY),
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

  const imageRef = useRef<any>(null);

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
        onClick={onSelect}
        onTap={onSelect}
        onTransformEnd={(e: any) => {
          const node = imageRef.current;
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
