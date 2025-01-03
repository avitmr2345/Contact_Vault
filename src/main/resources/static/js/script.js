console.log("Script loaded");
let currentTheme = getTheme();

document.addEventListener("DOMContentLoaded", () => {
  changeTheme();
  changePageTheme(currentTheme, currentTheme);
});

function changeTheme() {
  const changeThemeButton = document.querySelector("#theme_change_button");

  changeThemeButton.addEventListener("click", (event) => {
    console.log("change theme button clicked");

    currentTheme = currentTheme === "dark" ? "light" : "dark";

    changePageTheme(currentTheme);
  });
}

// Set theme to localStorage
function setTheme(theme) {
  localStorage.setItem("theme", theme);
}

// Get theme from localStorage
function getTheme() {
  let theme = localStorage.getItem("theme");
  return theme ? theme : "light";
}

// Change current page theme
function changePageTheme(theme) {
  setTheme(theme);

  const oldTheme = theme === "dark" ? "light" : "dark";

  document.querySelector("html").classList.remove(oldTheme);
  document.querySelector("html").classList.add(theme);

  document
    .querySelector("#theme_change_button")
    .querySelector("span").textContent = theme === "light" ? "Dark" : "Light";
}
