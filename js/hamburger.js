const toggle = document.querySelector(".nav-toggle");
const nav = document.querySelector("#primary-nav");

// Only run this code if BOTH elements actually exist on the page
if (toggle && nav) {
  toggle.addEventListener("click", () => {
    const isOpen = nav.classList.toggle("is-open"); // Adds/removes "is-open" to show/hide the menu, and returns true if it’s now open

    // Update accessibility attribute so screen readers know if the menu is open or closed
    if (isOpen) {
      toggle.setAttribute("aria-expanded", "true");
    } else {
      toggle.setAttribute("aria-expanded", "false");
    }
  });
}
