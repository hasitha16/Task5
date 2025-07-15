package com.example.task2.data;

import java.util.ArrayList;
import java.util.List;

import com.example.task2.models.Product;

public class ProductData {

    public static List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();

        // 10 Couch Products (Furniture)
        productList.add(new Product("couch1", "Luxury Leather Couch", "A comfortable luxury leather couch perfect for modern homes.", "Furniture", "https://images.pexels.com/photos/1648776/pexels-photo-1648776.jpeg?auto=compress&cs=tinysrgb&w=500", 499.99, 4.5f));
        productList.add(new Product("couch2", "Minimalist Grey Sofa", "A soft, minimalist couch for urban living rooms.", "Furniture", "https://images.pexels.com/photos/1571460/pexels-photo-1571460.jpeg?auto=compress&cs=tinysrgb&w=500", 389.00, 4.2f));
        productList.add(new Product("couch3", "Compact L-Shaped Couch", "Perfect for small apartments with space-saving design.", "Furniture", "https://images.pexels.com/photos/1571468/pexels-photo-1571468.jpeg?auto=compress&cs=tinysrgb&w=500", 359.99, 4.0f));
        productList.add(new Product("couch4", "Velvet Royal Couch", "Luxurious velvet with deep seating comfort.", "Furniture", "https://images.pexels.com/photos/1350789/pexels-photo-1350789.jpeg?auto=compress&cs=tinysrgb&w=500", 599.00, 4.6f));
        productList.add(new Product("couch5", "Modern Modular Sofa", "Reconfigurable for different spaces.", "Furniture", "https://images.pexels.com/photos/2029698/pexels-photo-2029698.jpeg?auto=compress&cs=tinysrgb&w=500", 449.00, 4.3f));
        productList.add(new Product("couch6", "Classic Blue Sofa", "Traditional style with premium fabric.", "Furniture", "https://images.pexels.com/photos/1866149/pexels-photo-1866149.jpeg?auto=compress&cs=tinysrgb&w=500", 399.99, 4.1f));
        productList.add(new Product("couch7", "Green Velvet Sofa", "Bold color and luxurious comfort.", "Furniture", "https://images.pexels.com/photos/2762247/pexels-photo-2762247.jpeg?auto=compress&cs=tinysrgb&w=500", 429.00, 4.5f));
        productList.add(new Product("couch8", "Convertible Sofa Bed", "Great for guests and dual functionality.", "Furniture", "https://images.pexels.com/photos/1444424/pexels-photo-1444424.jpeg?auto=compress&cs=tinysrgb&w=500", 549.00, 4.4f));
        productList.add(new Product("couch9", "Faux Leather Recliner", "Adjustable and comfy for binge-watching.", "Furniture", "https://images.pexels.com/photos/3773575/pexels-photo-3773575.jpeg?auto=compress&cs=tinysrgb&w=500", 499.00, 4.2f));
        productList.add(new Product("couch10", "Minimal Cream Couch", "Neutral tones and simple elegance.", "Furniture", "https://images.pexels.com/photos/2082090/pexels-photo-2082090.jpeg?auto=compress&cs=tinysrgb&w=500", 379.00, 4.0f));

        // 10 Bed Products (Bedroom)
        productList.add(new Product("bed1", "King Size Wooden Bed", "A sturdy and spacious wooden bed for restful sleep.", "Bedroom", "https://images.pexels.com/photos/164595/pexels-photo-164595.jpeg?auto=compress&cs=tinysrgb&w=500", 799.00, 4.7f));
        productList.add(new Product("bed2", "Upholstered Queen Bed", "Elegant and modern upholstered bed with headboard.", "Bedroom", "https://images.pexels.com/photos/1743229/pexels-photo-1743229.jpeg?auto=compress&cs=tinysrgb&w=500", 699.00, 4.4f));
        productList.add(new Product("bed3", "Storage Bed Frame", "Extra drawers for your clutter.", "Bedroom", "https://images.pexels.com/photos/1329711/pexels-photo-1329711.jpeg?auto=compress&cs=tinysrgb&w=500", 649.00, 4.2f));
        productList.add(new Product("bed4", "Low Platform Bed", "Modern minimalism meets functionality.", "Bedroom", "https://images.pexels.com/photos/1034584/pexels-photo-1034584.jpeg?auto=compress&cs=tinysrgb&w=500", 589.00, 4.1f));
        productList.add(new Product("bed5", "Rustic Log Bed", "Natural wood finish for cozy cabins.", "Bedroom", "https://images.pexels.com/photos/2177482/pexels-photo-2177482.jpeg?auto=compress&cs=tinysrgb&w=500", 859.00, 4.6f));
        productList.add(new Product("bed6", "Canopy Queen Bed", "Dreamy and elegant with canopy structure.", "Bedroom", "https://images.pexels.com/photos/1648771/pexels-photo-1648771.jpeg?auto=compress&cs=tinysrgb&w=500", 949.00, 4.5f));
        productList.add(new Product("bed7", "Simple Twin Bed", "Ideal for kids' or guest rooms.", "Bedroom", "https://images.pexels.com/photos/1457842/pexels-photo-1457842.jpeg?auto=compress&cs=tinysrgb&w=500", 399.00, 4.0f));
        productList.add(new Product("bed8", "Sleigh Style Bed", "Classic curves with bold design.", "Bedroom", "https://images.pexels.com/photos/2724749/pexels-photo-2724749.jpeg?auto=compress&cs=tinysrgb&w=500", 789.00, 4.3f));
        productList.add(new Product("bed9", "Metal Frame Bed", "Minimal, sturdy and lightweight.", "Bedroom", "https://images.pexels.com/photos/1090638/pexels-photo-1090638.jpeg?auto=compress&cs=tinysrgb&w=500", 329.00, 3.9f));
        productList.add(new Product("bed10", "Hydraulic Lift Bed", "Lift mattress to reveal full storage space.", "Bedroom", "https://images.pexels.com/photos/2995012/pexels-photo-2995012.jpeg?auto=compress&cs=tinysrgb&w=500", 899.00, 4.4f));

        // 10 Vase Products (Decor)
        productList.add(new Product("vase1", "Handcrafted Ceramic Vase", "Beautifully handcrafted ceramic vase with floral patterns.", "Decor", "https://images.pexels.com/photos/1797121/pexels-photo-1797121.jpeg?auto=compress&cs=tinysrgb&w=500", 79.99, 4.8f));
        productList.add(new Product("vase2", "Minimalist Glass Vase", "Perfect for fresh flowers or as a centerpiece.", "Decor", "https://images.pexels.com/photos/1519778/pexels-photo-1519778.jpeg?auto=compress&cs=tinysrgb&w=500", 59.99, 4.5f));
        productList.add(new Product("vase3", "Rustic Clay Vase", "Earthy tones with rough textures.", "Decor", "https://images.pexels.com/photos/2040627/pexels-photo-2040627.jpeg?auto=compress&cs=tinysrgb&w=500", 49.00, 4.2f));
        productList.add(new Product("vase4", "Tall Floor Vase", "Ideal for dry branches and tall arrangements.", "Decor", "https://images.pexels.com/photos/1404819/pexels-photo-1404819.jpeg?auto=compress&cs=tinysrgb&w=500", 99.00, 4.6f));
        productList.add(new Product("vase5", "Marble Finish Vase", "Elegant marble look for modern interiors.", "Decor", "https://images.pexels.com/photos/1707821/pexels-photo-1707821.jpeg?auto=compress&cs=tinysrgb&w=500", 89.00, 4.4f));
        productList.add(new Product("vase6", "Bohemian Painted Vase", "Colorful, handcrafted design.", "Decor", "https://images.pexels.com/photos/1643383/pexels-photo-1643383.jpeg?auto=compress&cs=tinysrgb&w=500", 69.00, 4.3f));
        productList.add(new Product("vase7", "Vintage Brass Vase", "Antique charm for vintage lovers.", "Decor", "https://images.pexels.com/photos/2635038/pexels-photo-2635038.jpeg?auto=compress&cs=tinysrgb&w=500", 119.00, 4.7f));
        productList.add(new Product("vase8", "Clear Cylinder Vase", "Simple and timeless.", "Decor", "https://images.pexels.com/photos/1266302/pexels-photo-1266302.jpeg?auto=compress&cs=tinysrgb&w=500", 39.99, 4.0f));
        productList.add(new Product("vase9", "Geometric Cut Vase", "Contemporary design with edgy cuts.", "Decor", "https://images.pexels.com/photos/2079246/pexels-photo-2079246.jpeg?auto=compress&cs=tinysrgb&w=500", 59.00, 4.1f));
        productList.add(new Product("vase10", "Twin Set Vases", "Two-piece ceramic vase set for paired aesthetics.", "Decor", "https://images.pexels.com/photos/1404826/pexels-photo-1404826.jpeg?auto=compress&cs=tinysrgb&w=500", 109.00, 4.5f));

        // 10 Dining Set Products (Dining)
        productList.add(new Product("dining1", "6-Seater Oak Dining Set", "Elegant oak dining set perfect for family dinners.", "Dining", "https://images.pexels.com/photos/1080721/pexels-photo-1080721.jpeg?auto=compress&cs=tinysrgb&w=500", 899.00, 4.6f));
        productList.add(new Product("dining2", "Modern Glass Dining Table", "Stylish dining table with tempered glass top.", "Dining", "https://images.pexels.com/photos/1571453/pexels-photo-1571453.jpeg?auto=compress&cs=tinysrgb&w=500", 749.00, 4.3f));
        productList.add(new Product("dining3", "4-Seater Marble Top Set", "Compact yet luxurious.", "Dining", "https://images.pexels.com/photos/1395967/pexels-photo-1395967.jpeg?auto=compress&cs=tinysrgb&w=500", 649.00, 4.4f));
        productList.add(new Product("dining4", "Expandable Dining Table", "Seats 4 to 8 people easily.", "Dining", "https://images.pexels.com/photos/4857776/pexels-photo-4857776.jpeg?auto=compress&cs=tinysrgb&w=500", 799.00, 4.2f));
        productList.add(new Product("dining5", "Industrial Style Table", "Metal and wood fusion design.", "Dining", "https://images.pexels.com/photos/1358912/pexels-photo-1358912.jpeg?auto=compress&cs=tinysrgb&w=500", 699.00, 4.1f));
        productList.add(new Product("dining6", "Round Wood Dining Set", "Warm, circular seating for close conversation.", "Dining", "https://images.pexels.com/photos/2995012/pexels-photo-2995012.jpeg?auto=compress&cs=tinysrgb&w=500", 579.00, 4.5f));
        productList.add(new Product("dining7", "White Scandinavian Set", "Clean lines and bright minimalism.", "Dining", "https://images.pexels.com/photos/1833319/pexels-photo-1833319.jpeg?auto=compress&cs=tinysrgb&w=500", 689.00, 4.3f));
        productList.add(new Product("dining8", "Rustic Farmhouse Table", "Chunky wood with countryside vibe.", "Dining", "https://images.pexels.com/photos/1457847/pexels-photo-1457847.jpeg?auto=compress&cs=tinysrgb&w=500", 799.00, 4.6f));
        productList.add(new Product("dining9", "Foldable Dining Set", "Perfect for small homes or balconies.", "Dining", "https://images.pexels.com/photos/2089698/pexels-photo-2089698.jpeg?auto=compress&cs=tinysrgb&w=500", 499.00, 4.0f));
        productList.add(new Product("dining10", "Glass & Gold Frame Table", "Luxurious look with a modern edge.", "Dining", "https://images.pexels.com/photos/1571461/pexels-photo-1571461.jpeg?auto=compress&cs=tinysrgb&w=500", 849.00, 4.7f));

        // 10 Chair Products (Chair)
        productList.add(new Product("chair1", "Classic Wooden Chair", "Timeless design made from solid wood.", "Chairs", "https://images.pexels.com/photos/731928/pexels-photo-731928.jpeg?auto=compress&cs=tinysrgb&w=500", 89.00, 4.5f));
        productList.add(new Product("chair2", "Modern Grey Armchair", "Perfect for contemporary interiors.", "Chairs", "https://images.pexels.com/photos/276551/pexels-photo-276551.jpeg?auto=compress&cs=tinysrgb&w=500", 120.00, 4.4f));
        productList.add(new Product("chair3", "Minimalist White Chair", "Lightweight and stylish.", "Chairs", "https://images.pexels.com/photos/139816/pexels-photo-139816.jpeg?auto=compress&cs=tinysrgb&w=500", 75.00, 4.2f));
        productList.add(new Product("chair4", "Retro Leather Seat", "Vintage vibes with comfort.", "Chairs", "https://images.pexels.com/photos/693269/pexels-photo-693269.jpeg?auto=compress&cs=tinysrgb&w=500", 149.00, 4.6f));
        productList.add(new Product("chair5", "Cozy Blue Lounge Chair", "Relax in style and color.", "Chairs", "https://images.pexels.com/photos/209984/pexels-photo-209984.jpeg?auto=compress&cs=tinysrgb&w=500", 134.00, 4.3f));
        productList.add(new Product("chair6", "Simple Office Chair", "Comfortable back support for long hours.", "Chairs", "https://images.pexels.com/photos/1639523/pexels-photo-1639523.jpeg?auto=compress&cs=tinysrgb&w=500", 99.00, 4.0f));
        productList.add(new Product("chair7", "Rattan Accent Chair", "Handwoven beauty for natural interiors.", "Chairs", "https://images.pexels.com/photos/280221/pexels-photo-280221.jpeg?auto=compress&cs=tinysrgb&w=500", 145.00, 4.4f));
        productList.add(new Product("chair8", "Folding Wooden Chair", "Great for extra seating.", "Chairs", "https://images.pexels.com/photos/328490/pexels-photo-328490.jpeg?auto=compress&cs=tinysrgb&w=500", 55.00, 4.1f));
        productList.add(new Product("chair9", "Patterned Reading Chair", "Stylish and comfortable for reading corners.", "Chairs", "https://images.pexels.com/photos/1264864/pexels-photo-1264864.jpeg?auto=compress&cs=tinysrgb&w=500", 159.00, 4.5f));
        productList.add(new Product("chair10", "High-back Velvet Chair", "Luxurious and supportive.", "Chairs", "https://images.pexels.com/photos/1866149/pexels-photo-1866149.jpeg?auto=compress&cs=tinysrgb&w=500", 189.00, 4.7f));


        // 10 Cupboard Products (Cupboard)
        productList.add(new Product("cupboard1", "Rustic Wooden Cupboard", "Farmhouse-style with natural tones.", "Cupboard", "https://images.pexels.com/photos/1457842/pexels-photo-1457842.jpeg?auto=compress&cs=tinysrgb&w=500", 299.00, 4.6f));
        productList.add(new Product("cupboard2", "White Modular Storage", "Clean and efficient space management.", "Cupboard", "https://images.pexels.com/photos/273238/pexels-photo-273238.jpeg?auto=compress&cs=tinysrgb&w=500", 249.00, 4.2f));
        productList.add(new Product("cupboard3", "Dark Oak Wardrobe", "Sleek modern cupboard with handles.", "Cupboard", "https://images.pexels.com/photos/373945/pexels-photo-373945.jpeg?auto=compress&cs=tinysrgb&w=500", 379.00, 4.5f));
        productList.add(new Product("cupboard4", "Two-Door Classic Cupboard", "Traditional design with ample space.", "Cupboard", "https://images.pexels.com/photos/1643383/pexels-photo-1643383.jpeg?auto=compress&cs=tinysrgb&w=500", 319.00, 4.3f));
        productList.add(new Product("cupboard5", "Multi-shelf Compact Cupboard", "Great for small apartments.", "Cupboard", "https://images.pexels.com/photos/2635038/pexels-photo-2635038.jpeg?auto=compress&cs=tinysrgb&w=500", 199.00, 4.0f));
        productList.add(new Product("cupboard6", "Minimalist Open Cabinet", "Chic and space-saving.", "Cupboard", "https://images.pexels.com/photos/1266302/pexels-photo-1266302.jpeg?auto=compress&cs=tinysrgb&w=500", 279.00, 4.4f));
        productList.add(new Product("cupboard7", "Gray Metal Utility Closet", "Tough and modern.", "Cupboard", "https://images.pexels.com/photos/2079246/pexels-photo-2079246.jpeg?auto=compress&cs=tinysrgb&w=500", 329.00, 4.1f));
        productList.add(new Product("cupboard8", "Elegant Brown Cabinet", "Perfect for living room storage.", "Cupboard", "https://images.pexels.com/photos/1404826/pexels-photo-1404826.jpeg?auto=compress&cs=tinysrgb&w=500", 345.00, 4.5f));
        productList.add(new Product("cupboard9", "Paneled Bedroom Cupboard", "Traditional paneled design.", "Cupboard", "https://images.pexels.com/photos/2724749/pexels-photo-2724749.jpeg?auto=compress&cs=tinysrgb&w=500", 299.00, 4.3f));
        productList.add(new Product("cupboard10", "Slim Wooden Bookshelf", "Also serves as a cupboard for essentials.", "Cupboard", "https://images.pexels.com/photos/2995012/pexels-photo-2995012.jpeg?auto=compress&cs=tinysrgb&w=500", 229.00, 4.2f));


        // 12 Lighting Products (Lighting)
        productList.add(new Product("light1", "Minimalist Desk Lamp", "Sleek modern desk lamp perfect for focused work.", "Lighting", "https://images.pexels.com/photos/110954/pexels-photo-110954.jpeg?auto=compress&cs=tinysrgb&w=500", 49.99, 4.4f));
        productList.add(new Product("light2", "Vintage Edison Bulb Lamp", "Classic Edison bulb lamp with warm glow.", "Lighting", "https://images.pexels.com/photos/271618/pexels-photo-271618.jpeg?auto=compress&cs=tinysrgb&w=500", 59.99, 4.6f));
        productList.add(new Product("light3", "Wood Table Lamp", "Elegant wooden table lamp for cozy interiors.", "Lighting", "https://images.pexels.com/photos/590493/pexels-photo-590493.jpeg?auto=compress&cs=tinysrgb&w=500", 79.00, 4.3f));
        productList.add(new Product("light4", "Modern Bedside Lamp", "Contemporary bedside lamp with soft light.", "Lighting", "https://images.pexels.com/photos/271624/pexels-photo-271624.jpeg?auto=compress&cs=tinysrgb&w=500", 65.00, 4.5f));
        productList.add(new Product("light5", "Industrial Cage Pendant", "Industrial-style cage pendant lamp.", "Lighting", "https://images.pexels.com/photos/257797/pexels-photo-257797.jpeg?auto=compress&cs=tinysrgb&w=500", 99.99, 4.2f));
        productList.add(new Product("light6", "Wicker Hanging Lamp", "Boho wicker hanging lamp for living rooms.", "Lighting", "https://images.pexels.com/photos/1457847/pexels-photo-1457847.jpeg?auto=compress&cs=tinysrgb&w=500", 89.00, 4.4f));
        productList.add(new Product("light7", "Neon Accent Lamp", "Colorful neon accent lamp for modern spaces.", "Lighting", "https://images.pexels.com/photos/277909/pexels-photo-277909.jpeg?auto=compress&cs=tinysrgb&w=500", 120.00, 4.1f));
        productList.add(new Product("light8", "Minimal Floor Lamp", "Slim minimal floor lamp for corners.", "Lighting", "https://images.pexels.com/photos/271618/pexels-photo-271618.jpeg?auto=compress&cs=tinysrgb&w=500", 149.00, 4.3f));
        productList.add(new Product("light9", "Glass Globe Lamp", "Transparent glass globe lamp with elegant base.", "Lighting", "https://images.pexels.com/photos/1553963/pexels-photo-1553963.jpeg?auto=compress&cs=tinysrgb&w=500", 110.00, 4.5f));
        productList.add(new Product("light10", "Brass Desk Lamp", "Brass-finished desk lamp adding a vintage touch.", "Lighting", "https://images.pexels.com/photos/7096/wood-desk-office-workspace.jpg?auto=compress&cs=tinysrgb&w=500", 99.00, 4.0f));
        productList.add(new Product("light11", "Smart RGB Lamp", "Stylish RGB lamp controllable via app.", "Lighting", "https://images.pexels.com/photos/373947/pexels-photo-373947.jpeg?auto=compress&cs=tinysrgb&w=500", 129.00, 4.7f));
        productList.add(new Product("light12", "Ceramic Bedside Lamp", "Handcrafted ceramic lamp with soft glow.", "Lighting", "https://images.pexels.com/photos/1983035/pexels-photo-1983035.jpeg?auto=compress&cs=tinysrgb&w=500", 75.00, 4.4f));

        return productList;
    }

    public static Product getProductById(String id) {
        for (Product p : getAllProducts()) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    public static List<Product> getProductsByCategory(String category) {
        List<Product> filteredProducts = new ArrayList<>();
        for (Product p : getAllProducts()) {
            if (p.getCategory().equals(category)) {
                filteredProducts.add(p);
            }
        }
        return filteredProducts;
    }

    public static List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("All");
        categories.add("Furniture");
        categories.add("Bedroom");
        categories.add("Decor");
        categories.add("Dining");
        categories.add("Chairs");
        categories.add("Cupboard");
        categories.add("Lighting");

        return categories;
    }
}