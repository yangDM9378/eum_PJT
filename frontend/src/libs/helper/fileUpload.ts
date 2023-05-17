import AWS from "aws-sdk";

const s3 = new AWS.S3({
  accessKeyId: process.env.NEXT_PUBLIC_AWS_ACCESS_KEY_ID,
  secretAccessKey: process.env.NEXT_PUBLIC_AWS_SECRET_ACCESS_KEY,
  region: process.env.NEXT_PUBLIC_AWS_REGION,
});

const uploadImageToS3 = async (base64Image: string, filename: string) => {
  // Remove 'data:image/png;base64,' from the string
  const base64Data = base64Image.replace(/^data:image\/\w+;base64,/, ""); // Convert the base64 string to a buffer
  const dataBuffer = Buffer.from(base64Data, "base64");
  const BUCKET_NAME = process.env.NEXT_PUBLIC_S3_BUCKET_NAME;

  if (!BUCKET_NAME) {
    throw new Error("S3_BUCKET_NAME environment variable is not defined");
  }

  const params = {
    Bucket: BUCKET_NAME,
    Key: filename,
    Body: dataBuffer,
    ContentType: "image/png",
  };

  try {
    const s3Response = await s3.upload(params).promise();
    return s3Response.Location;
  } catch (error) {
    throw error;
  }
};

export default uploadImageToS3;
