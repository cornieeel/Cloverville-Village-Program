document.addEventListener('DOMContentLoaded', () => {
  // Run this only after the HTML is fully loaded, so elements like .tasks-grid exist
  fetch('./trades.json') // Get the JSON file that contains the trades/items data
    .then(response => response.json()) // Convert the response into a JavaScript array/object
    .then(data => {
      const taskGrid = document.querySelector(".tasks-grid"); // The container where all cards will be added

      // Go through each item from the JSON and create one card for it
      data.forEach(item => {
        const article = document.createElement("article"); // Create a new <article> element for one card
        article.className = "task-card";

        // Build the card HTML and insert values from the current JSON item
        article.innerHTML = `
          <div class="task-icon-circle">
            <img
              src="images/tasks-symbol.png"
              alt="Task icon"
              class="task-icon"
            />
          </div>
          <div class="task-body">
            <h2 class="task-title">${item.goodToOffer}</h2>
            <p class="task-owner">Owner: ${item.residentName}</p>
            <p class="task-points">${item.price} points</p>
          </div>
        `;

        taskGrid.appendChild(article); // Add the finished card into the grid on the page
      });
    });
});