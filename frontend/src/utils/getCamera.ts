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

      // 비디오의 너비와 높이 가져오기
      const { videoWidth, videoHeight } = videoRef.current;

      // 비디오 화면이 꽉 차도록 조절하기
      const videoRatio = videoWidth / videoHeight;
      const windowRatio = window.innerWidth / window.innerHeight;
      if (videoRatio > windowRatio) {
        videoRef.current.style.width = "100%";
        videoRef.current.style.height = "auto";
      } else {
        videoRef.current.style.width = "auto";
        videoRef.current.style.height = "100%";
      }
    }
  } catch (err) {
    console.error(err);
  }
};
