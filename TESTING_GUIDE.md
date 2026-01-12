# Step-by-Step Testing Guide for Payment Integration

This guide will walk you through testing the Stripe payment integration in your Rent A Room application.

## Prerequisites Checklist

- [ ] Java 17+ installed
- [ ] Maven installed
- [ ] MySQL running and accessible
- [ ] Stripe account created (free at https://stripe.com)

---

## Step 1: Get Your Stripe Test API Keys

1. **Sign up/Login to Stripe**
   - Go to https://stripe.com
   - Create a free account (or login if you have one)

2. **Access API Keys**
   - Go to https://dashboard.stripe.com/test/apikeys
   - You'll see two keys:
     - **Publishable key** (starts with `pk_test_`)
     - **Secret key** (starts with `sk_test_`) - Click "Reveal test key" to see it

3. **Copy Both Keys**
   - Keep them handy for the next step

---

## Step 2: Configure Your Application

1. **Open** `src/main/resources/application.properties`

2. **Replace the placeholder keys** with your actual Stripe keys:
   ```properties
   stripe.secret.key=sk_test_YOUR_ACTUAL_SECRET_KEY_HERE
   stripe.public.key=pk_test_YOUR_ACTUAL_PUBLIC_KEY_HERE
   stripe.currency=usd
   ```

3. **Save the file**

---

## Step 3: Build and Run the Application

1. **Open terminal/command prompt** in your project directory

2. **Build the project** (downloads dependencies including Stripe SDK):
   ```bash
   mvn clean install
   ```

3. **Start the application**:
   ```bash
   mvn spring-boot:run
   ```

4. **Wait for startup** - You should see:
   ```
   Started RentARoomApplication in X.XXX seconds
   ```

5. **Open your browser** and go to: `http://localhost:8080`

---

## Step 4: Create a Test Booking

1. **Register/Login** to your application
   - If you don't have an account, register as a "Renter"

2. **Create or Find a Room** to book:
   - If you have rooms listed, browse and select one
   - If not, you may need to:
     - Register as a "Host" in a separate account
     - Create a room listing
     - Then login as "Renter" to book it

3. **Create a Booking**:
   - Select check-in and check-out dates
   - Click "Book Now" or similar button
   - You'll be redirected to the booking detail page

---

## Step 5: Test the Payment Flow

### What You Should See:
- Booking details (room, dates, price)
- A **"Complete Payment"** section with a payment form
- Card input fields (powered by Stripe Elements)

### Test Successful Payment:

1. **Enter Test Card Details**:
   ```
   Card Number: 4242 4242 4242 4242
   Expiry Date: 12/25 (any future date)
   CVC: 123 (any 3 digits)
   ZIP Code: 12345 (any 5 digits)
   ```

2. **Click "Pay Now"**

3. **Expected Result**:
   - Payment form disappears
   - Success message appears: "Payment successful! Your booking has been confirmed."
   - Page redirects or shows updated booking status
   - Booking status changes to "CONFIRMED"

### Test Failed Payment:

1. **Enter Declined Card**:
   ```
   Card Number: 4000 0000 0000 0002
   Expiry Date: 12/25
   CVC: 123
   ZIP Code: 12345
   ```

2. **Click "Pay Now"**

3. **Expected Result**:
   - Error message appears: "Your card was declined."
   - Payment form remains visible
   - Booking status stays "PENDING"

---

## Step 6: Verify in Stripe Dashboard

1. **Go to Stripe Dashboard**: https://dashboard.stripe.com/test/payments

2. **Check Payments List**:
   - You should see test payments you just made
   - Click on a payment to see details:
     - Amount
     - Status (succeeded/failed)
     - Payment method
     - Customer information

3. **Check Logs** (optional):
   - Go to https://dashboard.stripe.com/test/logs
   - See API requests and responses

---

## Step 7: Test API Endpoints Directly (Optional)

You can also test the payment API endpoints directly using a tool like Postman or curl:

### Test 1: Create Payment Intent

**Using curl:**
```bash
curl -X POST http://localhost:8080/api/payments/create-intent \
  -H "Content-Type: application/json" \
  -d '{"bookingId": 1, "amount": 150.00}'
```

**Expected Response:**
```json
{
  "id": 1,
  "bookingId": 1,
  "amount": 150.00,
  "paymentMethod": "card",
  "status": "PENDING",
  "clientSecret": "pi_xxxxx_secret_xxxxx",
  "paymentIntentId": "pi_xxxxx"
}
```

### Test 2: Get Payment Status

```bash
curl http://localhost:8080/api/payments/status/1
```

### Test 3: Confirm Payment

```bash
curl -X POST http://localhost:8080/api/payments/confirm \
  -H "Content-Type: application/json" \
  -d '{"paymentIntentId": "pi_xxxxx"}'
```

**Note:** You'll need to be logged in (session cookie) for these endpoints to work.

---

## Common Test Cards

| Card Number | Scenario | Expected Result |
|------------|----------|----------------|
| `4242 4242 4242 4242` | Success | Payment succeeds |
| `4000 0000 0000 0002` | Decline | Card declined |
| `4000 0025 0000 3155` | 3D Secure | Requires authentication |
| `4000 0000 0000 9995` | Insufficient funds | Payment fails |

**For all test cards:**
- Expiry: Any future date (e.g., 12/25)
- CVC: Any 3 digits (e.g., 123)
- ZIP: Any 5 digits (e.g., 12345)

More test cards: https://stripe.com/docs/testing

---

## Troubleshooting

### Issue: Payment form doesn't appear

**Check:**
1. ✅ Booking status is `PENDING`
2. ✅ Payment hasn't already been completed
3. ✅ Stripe public key is set correctly in `application.properties`
4. ✅ Browser console for JavaScript errors (F12 → Console tab)
5. ✅ Network tab to see if Stripe.js loaded

**Fix:**
- Check browser console for errors
- Verify `stripe.public.key` starts with `pk_test_`
- Make sure you're viewing a booking you created

---

### Issue: "Error creating payment intent"

**Check:**
1. ✅ Stripe secret key is correct in `application.properties`
2. ✅ Secret key starts with `sk_test_`
3. ✅ Application restarted after changing keys
4. ✅ Server logs for detailed error messages

**Fix:**
- Double-check the secret key (no extra spaces)
- Restart the application after changing keys
- Check server console for error details

---

### Issue: Payment succeeds but booking not confirmed

**Check:**
1. ✅ Database connection is working
2. ✅ Payment status in database
3. ✅ Server logs for errors

**Fix:**
- Check MySQL is running
- Verify database connection in `application.properties`
- Check server logs for transaction errors

---

### Issue: "User not authenticated" error

**Check:**
1. ✅ You're logged in to the application
2. ✅ Session hasn't expired
3. ✅ You're accessing your own booking

**Fix:**
- Login again
- Make sure you're testing with a booking you created

---

## Quick Test Checklist

- [ ] Application starts without errors
- [ ] Can create a booking
- [ ] Payment form appears on booking detail page
- [ ] Can enter card details
- [ ] Successful payment test works (`4242 4242 4242 4242`)
- [ ] Failed payment test works (`4000 0000 0000 0002`)
- [ ] Payment appears in Stripe dashboard
- [ ] Booking status updates to CONFIRMED after payment
- [ ] Payment details show on booking page after payment

---

## Next Steps After Testing

Once testing is successful:

1. **Test edge cases**:
   - Multiple payments for same booking
   - Payment retry after failure
   - Payment for cancelled booking

2. **Production readiness**:
   - Switch to live API keys (when ready)
   - Set up webhook signature verification
   - Add email notifications
   - Implement payment retry logic
   - Add comprehensive logging

3. **Security**:
   - Never commit API keys to version control
   - Use environment variables in production
   - Set up proper error handling

---

## Need Help?

- **Stripe Support**: https://support.stripe.com
- **Stripe Testing Docs**: https://stripe.com/docs/testing
- **Check server logs** for detailed error messages
- **Browser console** (F12) for frontend errors
