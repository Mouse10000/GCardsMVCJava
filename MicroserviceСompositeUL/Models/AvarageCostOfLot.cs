using System.Text.Json.Serialization;

namespace MicroserviceСompositeUL.Models
{
    public class AvarageCostLotofUser
    {
        [JsonPropertyName("userID")]
        public long UserID { get; set; }
        [JsonPropertyName("averageCostLots")]
        public double AverageCostLots { get; set; }
    }
}
