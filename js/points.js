document.addEventListener('DOMContentLoaded', () => {

  fetch('./actualPoints.json')
    .then(response => response.json()) // Convert the HTTP response into JS data
    .then(data => {
      let greenPointsTotal = 0; // We’ll add up all activity points into this number

      data.forEach(item => {
        greenPointsTotal += Number(item.pointsPerActivity);
      });

      // Update the UI based on the total points we calculated
      updateProgress(greenPointsTotal);
      updateNextReward(greenPointsTotal);
    });
});

function updateProgress(points) {
  const MAX_POINTS = 1500;

  // Convert points to a percent, but never above 100%
  const percent = Math.min((points / MAX_POINTS) * 100, 100);

  const fill = document.getElementById('points-progress');
  const pointsText = document.querySelector('.summary-value');

  // Set bar width like
  fill.style.width = percent + '%';
  pointsText.textContent = points;
}

function updateNextReward(points) {
  // Reward thresholds (when each reward becomes unlocked)
  const REWARD_1 = 500;
  const REWARD_2 = 1000;
  const REWARD_3 = 1500;

  // UI elements for the “next reward” message + percent message
  const nextRewardText = document.querySelector(".summary-goal");
  const nextRewardInPercentages = document.querySelector(".summary-note");

  // Reward cards (we change their CSS class to show done/next)
  const cardPizza = document.getElementById("pizza-card");
  const cardOutdoorNight = document.getElementById("outdoor-card");
  const cardWellness = document.getElementById("wellness-card");

  // Text labels that say whether each reward is unlocked
  const unlockedPizza = document.getElementById("pizza-unlocked");
  const unlockedOutdoor = document.getElementById("outdoor-unlocked");
  const unlockedWellness = document.getElementById("wellness-unlocked");

  // targetPoints = next threshold we’re aiming for
  // basePoints = last threshold we already reached (for “stage” progress calculation)
  let targetPoints = 0;
  let basePoints = 0;

  if (points < REWARD_1) {
    targetPoints = REWARD_1;
    basePoints = 0;
    nextRewardText.textContent = `Next reward: pizza party at ${REWARD_1} points.`;

    unlockedPizza.textContent = "Not yet unlocked";
    unlockedOutdoor.textContent = "Not yet unlocked";
    unlockedWellness.textContent = "Not yet unlocked";

  } else if (points < REWARD_2) {
    targetPoints = REWARD_2;
    basePoints = REWARD_1;
    nextRewardText.textContent = `Next reward: outdoor movie night at ${REWARD_2} points.`;
    cardPizza.classList.replace('reward-card-next', 'reward-card-done');

    unlockedPizza.textContent = "Already unlocked";
    unlockedOutdoor.textContent = "Not yet unlocked";
    unlockedWellness.textContent = "Not yet unlocked";

  } else if (points < REWARD_3) {
    targetPoints = REWARD_3;
    basePoints = REWARD_2;
    nextRewardText.textContent = `Next reward: wellness day at ${REWARD_3} points.`;
    cardPizza.classList.replace('reward-card-next', 'reward-card-done');
    cardOutdoorNight.classList.replace('reward-card-next', 'reward-card-done');

    unlockedPizza.textContent = "Already unlocked";
    unlockedOutdoor.textContent = "Already unlocked";
    unlockedWellness.textContent = "Not yet unlocked";

  } else {
    // Case 4: 1500+ → everything is unlocked, stop here
    nextRewardText.textContent = "All rewards unlocked!";
    nextRewardInPercentages.textContent = "";
    cardPizza.classList.replace('reward-card-next', 'reward-card-done');
    cardOutdoorNight.classList.replace('reward-card-next', 'reward-card-done');
    cardWellness.classList.replace('reward-card-next', 'reward-card-done');

    unlockedPizza.textContent = "Already unlocked";
    unlockedOutdoor.textContent = "Already unlocked";
    unlockedWellness.textContent = "Already unlocked";

    return; // Don’t calculate “next reward” percent anymore
  }

  // Stage progress: percent from basePoints → targetPoints
  // Example: points=650, base=500, target=1000 → (650-500)/(1000-500)=30%
  const percent = Math.min(
    Math.round(((points - basePoints) / (targetPoints - basePoints)) * 100),
    100
  );

  nextRewardInPercentages.textContent = `${percent} percent of the way to the next reward.`;
}
