using System.Text.Json.Serialization;

namespace MicroserviceСompositeUL.Models
{
    public class Lot
    {
        [JsonPropertyName("id")]
        public long Id { get; set; }
        [JsonPropertyName("type")]
        public string Type { get; set; }
        [JsonPropertyName("cost")]
        public long Cost { get; set; }
        [JsonPropertyName("status")]
        public string Status { get; set; }
        [JsonPropertyName("dateOfStartAuction")]
        public DateTime DateOfStartAuction { get; set; }
        [JsonPropertyName("dateOfEndAuction")]
        public DateTime DateOfEndAuction { get; set; }
        [JsonPropertyName("userId")]
        public long UserId { get; set; }

    }
}
