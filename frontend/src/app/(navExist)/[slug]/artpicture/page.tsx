import FramePicture from "@/components/artpicture/framePicture";

export default function ArtPicturePage({
  params,
}: {
  params: { slug: string };
}) {
  return (
    <div>
      <FramePicture />
    </div>
  );
}
