// CameraModule.ts
export const startCamera = async (
  videoRef: React.RefObject<HTMLVideoElement>,
  setIsCameraReady: React.Dispatch<React.SetStateAction<boolean>>
) => {
  try {
    const stream = await navigator.mediaDevices.getUserMedia({
      video: true,
    });
    if (videoRef.current) {
      videoRef.current.srcObject = stream;
      videoRef.current.play();
      setIsCameraReady(true);
    }
  } catch (err) {
    console.error(err);
  }
};
