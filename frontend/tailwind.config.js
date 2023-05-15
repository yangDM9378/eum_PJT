/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/pages/**/*.{js,ts,jsx,tsx}",
    "./src/components/**/*.{js,ts,jsx,tsx}",
    "./src/app/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      keyframes: {
        show: {
          "0%": { marginTop: "-300px" },
          "5%": { marginTop: "-180px" },
          "33%": { marginTop: "-180px" },
          "38%": { marginTop: "-90px" },
          "66%": { marginTop: "-90px" },
          "71%": { marginTop: "0px" },
          "99.99%": { marginTop: "0px" },
          "100%": { marginTop: "-270px" },
        },
      },
      animation: {
        show: "show 5s linear infinite",
      },
      backgroundImage: {
        "gradient-radial": "radial-gradient(var(--tw-gradient-stops))",
        "gradient-conic":
          "conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))",
      },
      colors: {
        "brand-baige": "#F8F9F3",
        "brand-red": "#D3A48D",
        "brand-green": "#77BC9E",
        "brand-pink": "#E9D1C6",
        "brand-blue": "#7592B0",
      },
      fontFamily: {
        "gmarket-thin": ["GmarketSansthin"],
        "gmarket-medium": ["GmarketSansMedium"],
        "gmarket-bold": ["GmarketSansBold"],
      },
    },
  },
  plugins: [],
};
