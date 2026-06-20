User: id, name, email(unique), password_hash, role[admin|employee], status[active|archived], created_at
Category: id, name, colors(array of hex strings)
Product: id, name, category_id->Category, price(decimal), uom(string), tax_pct(decimal), description(text,nullable), show_on_kds(boolean,default true)
PaymentMethod: id, name, type[cash|card|upi], upi_id(nullable), active(boolean)
Floor: id, name
Table: id, floor_id->Floor, number(int), seats(int), active(boolean), status[available|occupied](derived)
Coupon: id, name, code(unique), discount_type[percent|fixed], value(decimal), active(boolean)
Promotion: id, name, apply_scope[product|order], product_id(nullable)->Product, min_qty(nullable), min_order_amount(nullable), discount_type[percent|fixed], value(decimal), active(boolean)
Customer: id, name, email, phone
POSSession: id, employee_id->User, opened_at, closed_at(nullable), closing_amount(nullable)
Order: id, session_id->POSSession, table_id(nullable)->Table, customer_id(nullable)->Customer, order_number(sequential display), status[draft|paid|cancelled], source[pos|self_order], coupon_id(nullable)->Coupon, promotion_id(nullable)->Promotion, subtotal, tax_total, discount_total, total, created_at
OrderItem: id, order_id->Order, product_id->Product, qty(int), unit_price(decimal), line_total(decimal), kds_status[to_cook|preparing|completed]
Payment: id, order_id->Order, method_id->PaymentMethod, amount(decimal), cash_received(nullable), change_due(nullable), reference(nullable), status[pending|confirmed|cancelled], created_at
SelfOrderConfig: id, enabled(boolean), mode[online_ordering|qr_menu], bg_color(nullable), bg_images(array,nullable)
TableQRToken: id, table_id->Table, token(unique)
