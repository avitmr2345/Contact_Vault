const viewContactModal = document.getElementById("view_contact_modal");
const baseURL = "http://localhost:8080";

// options with default values
const options = {
  placement: "bottom-right",
  backdrop: "dynamic",
  backdropClasses: "bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40",
  closable: true,
  onHide: () => {
    console.log("modal is hidden");
  },
  onShow: () => {
    console.log("modal is shown");
  },
  onToggle: () => {
    console.log("modal has been toggled");
  },
};

// instance options object
const instanceOptions = {
  id: "view_contact_mdoal",
  override: true,
};

const contactModal = new Modal(viewContactModal, options, instanceOptions);
function openContactModal() {
  contactModal.show();
}

function closeContactModal() {
  contactModal.hide();
}

async function loadContactData(id) {
  console.log(id);
  try {
    const data = await (await fetch(`${baseURL}/api/contacts/${id}`)).json();
    console.log(data);

    document.querySelector("#contact_image").src = data.picture;
    document.querySelector("#contact_name").innerHTML = data.name;
    document.querySelector("#contact_email").innerHTML = data.email;
    document.querySelector("#contact_phone").innerHTML = data.phoneNumber;
    document.querySelector("#contact_address").innerHTML = data.address;

    if (data.description) {
      document.querySelector("#contact_about").innerHTML = data.description;
    } else {
      document.querySelector("#contact_about").innerHTML = "No Description";
    }

    const contactFavourite = document.querySelector("#contact_favourite");

    if (data.favourite) {
      contactFavourite.innerHTML =
        "<i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i>";
    } else {
      contactFavourite.innerHTML = "Not Favourite Contact";
    }

    if (data.instagramUsername) {
      document.querySelector("#contact_instagram").innerHTML =
        data.instagramUsername;
    } else {
      document.querySelector("#contact_instagram").innerHTML = "None";
    }

    if (data.linkedInLink) {
      document.querySelector("#contact_linkedIn").href = data.linkedInLink;
      document.querySelector("#contact_linkedIn").innerHTML = data.linkedInLink;
    } else {
      document.querySelector("#contact_linkedIn").innerHTML =
        "No Link Provided";
    }

    openContactModal();
  } catch (error) {
    console.log("Error: ", error);
  }
}
