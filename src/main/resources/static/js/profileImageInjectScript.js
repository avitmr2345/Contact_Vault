document
  .querySelector("#profile_image_input")
  .addEventListener("change", function (event) {
    let file = event.target.files[0];
    let reader = new FileReader();
    reader.onload = function () {
      document
        .querySelector("#upload_image_preview")
        .setAttribute("src", reader.result);
    };
    reader.readAsDataURL(file);
  });
