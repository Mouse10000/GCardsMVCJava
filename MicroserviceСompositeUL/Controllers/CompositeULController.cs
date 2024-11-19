
using Microsoft.AspNetCore.Mvc;
using MicroserviceСompositeUL.Models;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace MicroserviceСompositeUL.Controllers
{
    [ApiController]
    [Route("api/[controller]")]

    public class CompositeULController : ControllerBase
    {
        private readonly string _userServiceAddress = "https://localhost:7044/api/users";
        private readonly string _lotsServiceAddress = "https://localhost:7101/api/lots";

        [HttpGet("lots/{nameType}")]
        public async Task<List<Lot>> GetLotsByTypeAsync(string nameType)
        {
            HttpClientHandler clientHandler = new HttpClientHandler();
            clientHandler.ServerCertificateCustomValidationCallback =
                (sender, cert, chain, sslPolicyErrors) => { return true; };
            using (HttpClient client = new HttpClient(clientHandler))
            {
                HttpResponseMessage response = await
               client.GetAsync($"{_lotsServiceAddress}");
                if (response.IsSuccessStatusCode)
                {
                    List<Lot> types = await
                   JsonSerializer.DeserializeAsync<List<Lot>>(
                    await response.Content.ReadAsStreamAsync());
                    return types.Where(course =>
                    course.Type
                    .Split(',')
                    .Contains(nameType))
                    .ToList();
                }
            }
            return null;
        }
        [HttpGet]
        public string Start()
        {
            return "Composite is run!";
        }
        [HttpGet("users/{userID}")]
        public async Task<List<Lot>> GetLotsByUserAsync(long userID)
        {
            HttpClientHandler clientHandler = new HttpClientHandler();
            clientHandler.ServerCertificateCustomValidationCallback =
                (sender, cert, chain, sslPolicyErrors) => { return true; };
            using (HttpClient client = new HttpClient(clientHandler))
            {
                HttpResponseMessage response = await
               client.GetAsync($"{_lotsServiceAddress}");
                if (response.IsSuccessStatusCode)
                {
                    List<Lot> lots = await
                   JsonSerializer.DeserializeAsync<List<Lot>>(
                    await response.Content.ReadAsStreamAsync());
                    return lots.Where(lot => lot.UserId ==
                   userID)
                    .ToList();
                }
            }
            return null;
        }
        [HttpGet("cost/{userID}")]
        public async Task<AvarageCostLotofUser> GetAvarageCostsLotsOfUserAsync(long userID)
        {
            HttpClientHandler clientHandler = new HttpClientHandler();
            clientHandler.ServerCertificateCustomValidationCallback =
                (sender, cert, chain, sslPolicyErrors) => { return true; };
            using (HttpClient client = new HttpClient(clientHandler))
            {
                HttpResponseMessage response = await
               client.GetAsync($"{_lotsServiceAddress}");
                if (response.IsSuccessStatusCode)
                {
                    List<Lot> lots = await
                   JsonSerializer.DeserializeAsync<List<Lot>>(
                    await response.Content.ReadAsStreamAsync());
                    var collection = lots.Where(st => st.UserId ==
                   userID).ToList();
                    long costSum = 0;
                    foreach (var i in collection)
                        costSum += i.Cost;
                    return new AvarageCostLotofUser()
                    {
                        UserID = userID,
                        AverageCostLots = (double)costSum / collection.Count
                    };
                }
            }
            return null;
        }
        }
}
