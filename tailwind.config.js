/** @type {import('tailwindcss').Config} */
export default {
  content: ["./src/main/resources/**/*.{html,js}"],
  theme: {
    extend: {},
  },
  plugins: [],
  darkMode: "selector",
};
// Steps to include tailwind css -
// npx tailwindcss -i .\src\main\resources\static\css\input.css -o .\src\main\resources\static\css\output.css --watch
// Include output.css in your html
// If you want flowbite include css and js using cdn
