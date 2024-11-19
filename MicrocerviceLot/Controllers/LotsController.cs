
using System.Net.Http;
using System.Text.Json;

using Microsoft.AspNetCore.Mvc;


using Microsoft.EntityFrameworkCore;
using MicroserviceLot.Models;

namespace MicroserviceLot.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class LotsController : ControllerBase
    {
        private readonly LotContext _context;

        public LotsController(LotContext context)
        {
            _context = context;
        }

        // GET: api/Lots
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Lot>>> GetLot()
        {
            return await _context.Lot.ToListAsync();
        }

        // GET: api/Lots/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Lot>> GetLot(long id)
        {
            var Lot = await _context.Lot.FindAsync(id);

            if (Lot == null)
            {
                return NotFound();
            }

            return Lot;
        }

        // PUT: api/Lots/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutLot(long id, Lot lot)
        {
            if (id != lot.Id)
            {
                return BadRequest();
            }

            _context.Entry(lot).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!LotExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // POST: api/Lots
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<Lot>> PostLot(Lot lot)
        {
            _context.Lot.Add(lot);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetLot", new { id = lot.Id }, lot);
        }

        // DELETE: api/Lots/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteLot(long id)
        {
            var Lot = await _context.Lot.FindAsync(id);
            if (Lot == null)
            {
                return NotFound();
            }

            _context.Lot.Remove(Lot);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        //[HttpGet("user/{id}")]
        //public async Task<string> GetUser(long id)
        //{
        //    var actionResult = await GetLot(id);
        //    var lot = actionResult.Value;
        //    HttpClientHandler clientHandler = new HttpClientHandler();

        //    clientHandler.ServerCertificateCustomValidationCallback =
        //    (sender, cert, chain, sslPolicyErrors) => { return true; };
        //    using (HttpClient client = new HttpClient(clientHandler))
        //    {
        //        HttpResponseMessage response = await
        //        client.GetAsync($"https://localhost:7044/api/users/{lot.UserId}");
        //        if (response.IsSuccessStatusCode)
        //        {
        //           object user = await
        //           JsonSerializer.DeserializeAsync<object>(await
        //           response.Content.ReadAsStreamAsync());
        //                return user.ToString();
        //        }
        //    }
        //    return null;
        //}


        private bool LotExists(long id)
        {
            return _context.Lot.Any(e => e.Id == id);
        }
    }
}

