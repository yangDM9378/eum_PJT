/** @type {import('next').NextConfig} */
const nextConfig = {
  experimental: {
    appDir: true,
  },
  images: {
    domains: ["eumpyo-eum.s3.us-east-2.amazonaws.com"],
    remotePatterns: [
      {
        protocol: "https",
        hostname: "ai-result-rapidapi.ailabtools.com",
      },
    ],
  },
};

module.exports = nextConfig;
