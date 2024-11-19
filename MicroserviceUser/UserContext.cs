
using MicroserviceUser.Models;
using Microsoft.EntityFrameworkCore;

namespace MicroserviceUser
{
    public class UserContext : DbContext
    {
        public UserContext(DbContextOptions<UserContext> options)
            : base(options)
        {
        }
        public DbSet<User> User { get; set; }
    }
        
}