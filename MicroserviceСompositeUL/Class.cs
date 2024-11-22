using MicroserviceUser.Models;
using Microsoft.AspNetCore.Routing;
using NuGet.ContentModel;

namespace MicroserviceUser
{
    [TestFixture]
    public class CalorieCounterTests
    {
        private CalorieCounter _calorieCounter;

        [SetUp]
        public void Setup()
        {
            _calorieCounter = new CalorieCounter();
        }

        [Test]
        public void GetTotalCalories_EmptyList_ReturnsZero()
        {
            int result = _calorieCounter.GetTotalCalories();

            Assert.AreEqual(0, result);
        }

        [Test]
        public void GetTotalCalories_OneFoodItem_ReturnsCaloriesOfFoodItem()
        {
            _calorieCounter.AddFoodItem(new FoodItem("Apple", 100));

            int result = _calorieCounter.GetTotalCalories();

            Assert.AreEqual(100, result);
        }

        [Test]
        public void GetTotalCalories_MultipleFoodItems_ReturnsSumOfCalories()
        {
            _calorieCounter.AddFoodItem(new FoodItem("Apple", 100));
            _calorieCounter.AddFoodItem(new FoodItem("Banana", 150));
            _calorieCounter.AddFoodItem(new FoodItem("Orange", 75));

            int result = _calorieCounter.GetTotalCalories();

            Assert.AreEqual(325, result);
        }

        [Test]
        public void AddFoodItem_NullFoodItem_ThrowsArgumentNullException()
        {
            Assert.Throws<ArgumentNullException>(() => _calorieCounter.AddFoodItem(null));
        }

        [Test]
        public void GetTotalCalories_NegativeCalories_ThrowsArgumentOutOfRangeException()
        {
            _calorieCounter.AddFoodItem(new FoodItem("Apple", -100));

            Assert.Throws<ArgumentOutOfRangeException>(() => _calorieCounter.GetTotalCalories());
        }
    }
}
