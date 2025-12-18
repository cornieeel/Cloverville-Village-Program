const toggle = document.querySelector(".nav-toggle");
const nav = document.querySelector("#primary-nav");

if (toggle && nav) {
  toggle.addEventListener("click", () => {
    const isOpen = nav.classList.toggle("is-open");

    if (isOpen) {
      toggle.setAttribute("aria-expanded", "true");
    } else {
      toggle.setAttribute("aria-expanded", "false");
    }
  });
}