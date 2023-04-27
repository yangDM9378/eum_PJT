/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/pages/**/*.{js,ts,jsx,tsx}",
    "./src/components/**/*.{js,ts,jsx,tsx}",
    "./src/app/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
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
    },
    fontFamily: {
      "brand-gmarketsans": ["GmarketSans"],
      "brand-poppins": ["Poppins", "sans-serif"],
    },
    // backgroundColor: {
    //   "brand-baige": "#F8F9F3",
    //   "brand-red": "#D3A48D",
    //   "brand-green": "#77BC9E",
    //   "brand-pink": "#E9D1C6",
    // },
  },
  plugins: [],
};
