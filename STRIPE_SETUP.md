# Stripe Payment Integration Setup Guide

This guide will help you set up Stripe payment processing for Visa card payments in the Rent A Room application.

## Prerequisites

1. A Stripe account (sign up at https://stripe.com)
2. Java 17 or higher
3. Maven 3.6+

## Step 1: Get Your Stripe API Keys

1. Log in to your Stripe Dashboard: https://dashboard.stripe.com
2. Navigate to **Developers** → **API keys**
3. Copy your **Publishable key** (starts with `pk_test_` for test mode)
4. Copy your **Secret key** (starts with `sk_test_` for test mode)
   - Click "Reveal test key" to see it

**Important:** 
- Use test keys (`pk_test_` and `sk_test_`) for development
- Use live keys (`pk_live_` and `sk_live_`) only in production

## Step 2: Configure Application Properties

Edit `src/main/resources/application.properties` and replace the placeholder values:

```properties
# Stripe Configuration
stripe.secret.key=sk_test_YOUR_SECRET_KEY_HERE
stripe.public.key=pk_test_YOUR_PUBLIC_KEY_HERE
stripe.currency=usd
```

Replace `YOUR_SECRET_KEY_HERE` and `YOUR_PUBLIC_KEY_HERE` with your actual Stripe keys.

## Step 3: Test the Integration

### Using Test Cards

Stripe provides test card numbers for testing. Use these in the payment form:

**Successful Payment:**
- Card Number: `4242 4242 4242 4242`
- Expiry: Any future date (e.g., `12/25`)
- CVC: Any 3 digits (e.g., `123`)
- ZIP: Any 5 digits (e.g., `12345`)

**Declined Payment:**
- Card Number: `4000 0000 0000 0002`

**Requires Authentication (3D Secure):**
- Card Number: `4000 0025 0000 3155`

For more test cards, visit: https://stripe.com/docs/testing

### Testing Flow

1. Start your Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

2. Create a booking through the application

3. Navigate to the booking detail page

4. You should see a payment form

5. Enter test card details and complete the payment

6. Check your Stripe Dashboard → **Payments** to see the test payment

## API Endpoints

The payment integration provides the following REST endpoints:

### Create Payment Intent
```
POST /api/payments/create-intent
Content-Type: application/json

{
  "bookingId": 1,
  "amount": 150.00
}
```

### Confirm Payment
```
POST /api/payments/confirm
Content-Type: application/json

{
  "paymentIntentId": "pi_xxxxx"
}
```

### Get Payment Status
```
GET /api/payments/status/{bookingId}
```

## Security Notes

1. **Never commit your secret keys** to version control
2. Use environment variables or secure configuration management in production
3. The secret key should only be used on the server side
4. The public key is safe to expose in frontend code

## Production Deployment

Before going live:

1. Switch to live API keys in your production environment
2. Set up webhook endpoints to handle payment status updates
3. Implement proper error handling and logging
4. Add payment retry logic for failed payments
5. Set up email notifications for successful payments
6. Configure Stripe webhooks to notify your application of payment events

## Troubleshooting

### Payment form not showing
- Check that Stripe public key is correctly set in `application.properties`
- Verify the booking status is `PENDING`
- Check browser console for JavaScript errors

### Payment fails
- Verify Stripe secret key is correct
- Check server logs for error messages
- Ensure you're using test cards in test mode
- Verify the amount is correct (Stripe uses cents, but we convert automatically)

### "Payment intent not found" error
- Ensure the payment intent was created successfully
- Check that the payment intent ID matches between frontend and backend

## Additional Resources

- Stripe Documentation: https://stripe.com/docs
- Stripe Java SDK: https://github.com/stripe/stripe-java
- Stripe Testing: https://stripe.com/docs/testing
- Stripe Dashboard: https://dashboard.stripe.com

## Support

For Stripe-specific issues, contact Stripe Support: https://support.stripe.com
