
using MicroserviceLot.Models;
using Microsoft.EntityFrameworkCore;

namespace MicroserviceLot
{
    public class LotContext : DbContext

{
        public LotContext(DbContextOptions<LotContext> options)
        : base(options)
        {
        }
        public DbSet<Lot> Lot { get; set; }
    }

}
