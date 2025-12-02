# 📘 **CNS 3201 — INTERNETWORKING COMMUNICATION SYSTEMS**
## **FULL STUDY GUIDE MASTERBOOK (Enhanced)**

---

## **📚 PART 1 — LECTURE OVERVIEW & TOPIC MAP**

| **Lecture** | **Topics Covered** | **Key Exam Focus** |
|-------------|-------------------|---------------------|
| **Lec 1–2** | Intro to Internetworking, DNS Fundamentals, Resource Records (A, AAAA, MX, PTR, CNAME, TXT) | DNS resolution, caching, record types, troubleshooting; show `dig` examples |
| **Lec 3** | Advanced DNS: DNSSEC, GeoDNS, Load Balancing, CDN Integration | DNSSEC validation, GeoDNS for latency, DNS-based load balancing; TTL strategy |
| **Lec 4** | Web Systems: HTTP/HTTPS, REST vs SOAP, HAProxy, API Design | REST principles, HTTP status codes, reverse proxy config, API security |
| **Lec 5–6** | IAM: LDAP, SSO, OAuth2, Keycloak | LDAP tree design, OAuth2 flows, token lifecycle, Keycloak realms/clients |
| **Lec 7** | Email Systems: SMTP, IMAP, POP3, Postfix, Dovecot, SPF/DKIM/DMARC | SPF/DKIM/DMARC example records, Postfix queue troubleshooting |
| **Lec 8** | SMS & IoT Integration, REST APIs for Mobile Payments | Webhook design, idempotent callback handling, MQTT basics |
| **Lec 9** | SNMP: v2c vs v3, Traps vs Polling, MIBs, Network Monitoring | SNMPv3 security, trap vs poll tradeoffs, key MIB OIDs |
| **Lec 10–11** | RADIUS: AAA Framework, 802.1X, EAP Methods, WiFi NAC | 802.1X flow, EAP types (PEAP/TLS), RADIUS-LDAP integration and logging |
| **Lec 12** | Mobile Payments (MPESA Daraja API), Callback Handling, API Gateway | STK Push reliability, callback validation, idempotency and replay protection |
| **Lec 13** | Capstone Integration & Troubleshooting: LDAP → SMS → SIEM | Cross-system troubleshooting, correlation IDs, end-to-end logging |

---

### **📖 Reference Texts & Tools**
- **Primary Texts**:
  - Peterson & Davie – *Computer Networks: A Systems Approach*
  - Kurose & Ross – *Computer Networking: A Top-Down Approach*
- **Lab Tools & Commands**:
  - DNS: BIND9, `dig @<server> example.com ANY +short`
  - Mail: Postfix (`postfix status`, `mailq`, `postcat -q <id>`)
  - IAM: OpenLDAP (`ldapsearch -x -LLL -H ldap://localhost -b dc=example,dc=com`), Keycloak admin UI
  - Load balancing: HAProxy config snippets, `haproxy -c -f /etc/haproxy/haproxy.cfg`
- **Online Resources**: IETF RFCs (DNS, SMTP, RADIUS), OAuth2 Documentation, Safaricom Daraja API Docs

---

## **📝 PART 2 — HIGH-LEVEL CHEAT SHEET (ALL TOPICS)**

### **🌐 DNS (Domain Name System)**
- **Why it matters**: DNS is the naming backbone — slow or incorrect DNS causes almost every service to fail (web, email, APIs).
- **Core concepts (short)**:
  - Recursive vs Iterative: Recursive resolver fetches the full answer for the client; iterative returns referrals for the client to follow.
  - Authoritative server: holds zone data; resolvers cache results.
- **Record Types & examples**:
  - `A` / `AAAA` — host to IP (e.g., `www.example.com A 198.51.100.5`).
  - `MX` — mail exchanger (e.g., `example.com MX 10 mail.example.com`).
  - `CNAME` — alias (don't mix with other records on same name).
  - `TXT` — arbitrary text (SPF/DKIM/DMARC policies live here).
  - `PTR` — reverse pointer for rDNS; useful for email-reputation checks.
- **Quick commands**:
  - Check A record: `dig +noall +answer example.com A`
  - Trace resolution: `dig +trace example.com`
  - Check DNSSEC: `dig +dnssec example.com SOA` and look for RRSIG records.
- **Security & privacy**:
  - DNSSEC signs records to protect against spoofing — know the chain-of-trust concept.
  - DoT/DoH encrypt DNS queries to prevent on-path observation.
- **Troubleshooting checklist**:
  - Start with `dig` against authoritative server: `dig @ns1.example.com example.com SOA`.
  - Check TTLs and cached values: reduce TTL while rolling changes.
  - For mail issues, confirm MX + PTR + SPF alignment.
- **Exam tip**: When asked to resolve a DNS failure, list (1) what you check (`dig` commands), (2) why each check matters, (3) immediate mitigation (e.g., flush cache, lower TTL, fix zone file), and (4) preventive measure (monitoring, DNSSEC).

---

### **📧 EMAIL SYSTEMS (SMTP, IMAP, POP3)**
- **Why it matters**: Email remains a critical system for notifications and authentication — delivery and reputation are the top concerns.
- **Protocols & ports**:
  - SMTP: send/relay (`25` for server-to-server, `587` for client submission with auth).
  - IMAP: mailbox synchronization (`143`/`993` for TLS).
  - POP3: simple download-and-delete model (`110`/`995` for TLS).
- **SPF / DKIM / DMARC explained**:
  - SPF: declares authorized sending IPs in a TXT record (`v=spf1 a mx -all`).
  - DKIM: outbound signing using private key; public key stored in DNS TXT under `selector._domainkey`.
  - DMARC: policy combining SPF/DKIM results and instructing receivers (`v=DMARC1; p=quarantine; rua=mailto:dmarc@example.com`).
- **Practical checks & commands**:
  - Inspect queue: `postqueue -p` / `mailq`.
  - Inspect message: `postcat -q <queue-id>`.
  - Test SMTP conversation: `telnet mail.example.com 25` (or `openssl s_client -starttls smtp -crlf -connect mail.example.com:587`).
- **Common failure modes**:
  - Email rejected due to missing PTR or failing SPF/DKIM.
  - Deferred delivery due to DNS MX resolution or remote greylisting.
- **Exam tip**: For an email troubleshooting question, include checks for DNS (MX/PTR), SMTP logs, queue status, and reputation (blacklist lookups). Offer short-term mitigations (retry, notify users) and long-term fixes (correct DNS records, rotate keys, monitor bounce rates).

---

### **🕸️ WEB & APIS (HTTP, REST, SOAP)**
- **Core idea**: APIs expose functionality — design for simplicity, security, and observability.
- **REST quick checklist**:
  - Stateless requests, proper use of HTTP verbs and status codes (200/201/204/400/401/403/404/500).
  - Use pagination, filtering, and consistent JSON schemas.
- **SOAP when to expect it**:
  - Enterprise legacy systems with WSDL and strict contracts; use when message-level security or transactional guarantees are required.
- **HTTP/2 benefits & caveats**:
  - Multiplexing reduces head-of-line blocking; server push can be helpful but is rarely necessary in APIs.
- **API Gateway responsibilities**:
  - Authentication (JWT/OAuth2), rate limiting, routing, request/response transformation, TLS termination, logging/tracing.
- **Quick examples**:
  - Curl example (REST): `curl -i -H "Authorization: Bearer $TOKEN" https://api.example.com/v1/payments`
  - SOAP call: use `curl --data @request.xml -H "Content-Type: text/xml" https://legacy.example.com/service`.
- **Exam tip**: When designing an API, state the expected clients, security model (OAuth2/JWT), error-handling strategy, and monitoring approach (metrics + distributed tracing).

---

### **🔐 IAM / IDENTITY MANAGEMENT**
- **Why it matters**: Identity is the foundation of access control — get tokens, sessions, and revocation right.
- **LDAP practical notes**:
  - Typical DN pattern: `cn=alice,ou=users,dc=example,dc=com`.
  - Use `ldapsearch` to debug binding and searches.
- **SSO & OAuth2 practical flows**:
  - Authorization Code: interactive web apps — authorize, get code, exchange for token.
  - Client Credentials: machine-to-machine — no user consent, suitable for backend services.
  - PKCE: recommended for public/native apps replacing implicit.
- **Keycloak pointers**:
  - Realms isolate tenants; clients represent apps; use built-in token introspection and revocation.
- **Exam tip**: When asked to design IAM, include (1) user store choice (LDAP/DB), (2) flow selection (Auth Code vs Client Credentials), (3) token lifetime and refresh strategy, and (4) auditing and MFA choices.

---

### **📡 RADIUS & 802.1X**
- **Core concept**: RADIUS provides centralized auth for network access; 802.1X is the port-based network access control that uses RADIUS.
- **Key components**:
  - Supplicant (device) → Authenticator (switch/AP) → RADIUS server (FreeRADIUS).
- **EAP method guidance**:
  - EAP-TLS: best practice — requires cert management.
  - PEAP/EAP-MSCHAPv2: easier to deploy, relies on TLS tunnel protecting credentials.
- **Troubleshooting steps**:
  - Check NAS (AP/switch) logs for RADIUS timeouts.
  - Verify certificate chain on server and client.
  - Use `radclient`/`radtest` to simulate requests.
- **Exam tip**: For design questions, include certificate lifecycle, VLAN assignment, accounting logs for billing/auditing, and high availability (RADIUS proxies/load balancing).

---

### **📊 SNMP & NETWORK MONITORING**
- **Why it matters**: Monitoring gives early warning of failure — plan for metrics, logs, and alerts.
- **SNMP quick guide**:
  - v2c: simple but insecure (community strings).
  - v3: recommended for production — supports auth and encryption.
- **Polling vs Traps**:
  - Polling: periodic sampling for metrics; Traps: async alerts for events.
- **Useful commands**:
  - Get system name: `snmpget -v2c -c public router.example.com SNMPv2-MIB::sysName.0`
  - Walk interface MIB: `snmpwalk -v2c -c public router.example.com IF-MIB::ifTable`
- **Exam tip**: When asked to design monitoring, mention MIB selection, thresholding/alerting, storage (time-series DB), and retention/aggregation for trend analysis.

---

### **📞 VOIP (SIP & RTP)**
- **Core flow**: SIP handles signaling (call setup) and RTP carries media.
- **SIP message example**: INVITE → 180 → 200 → ACK. Know where SDP negotiates codecs/ports.
- **Network needs**:
  - Prioritize voice (DSCP EF), ensure low latency and appropriate jitter buffers.
- **Troubleshooting tips**:
  - One-way audio often points to RTP not being routed (NAT/firewall); inspect SDP addresses and use media relays (TURN/ALGs cautiously).
- **Exam tip**: Sketch a signaling timeline and then explain common failure patterns (NAT, codecs, firewall rules) with quick mitigations.

---

### **💰 MOBILE PAYMENTS (MPESA INTEGRATION)**
- **Overview**: Daraja (Safaricom) uses STK Push to prompt users; backend must handle callbacks reliably and idempotently.
- **Flow details**:
  - Initiation: service calls Daraja with credentials and amount; Daraja sends STK Push to user.
  - User approves and enters PIN; Daraja sends asynchronous callback to merchant endpoint.
  - Merchant verifies and updates DB; send user-facing confirmation.
- **Practical implementation notes**:
  - Secure endpoint: HTTPS, validate incoming signatures and timestamps, whitelist source IPs when possible.
  - Idempotency: store transaction references and ignore duplicate callbacks.
- **Command/example**:
  - cURL (simplified): `curl -X POST -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d @request.json https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest`
- **Exam tip**: In architecture answers, emphasise reliability (retries, dead-letter queues), security (TLS, input validation), and observability (logs and tracing).

---

## **🧩 PART 3 — INTEGRATED SCENARIO-BASED QUESTIONS & MODEL ANSWERS (Expanded Rubrics)**

### **🔹 Q1 — UNIVERSITY COMMUNICATION SYSTEM (30 MARKS)**
**Scenario**: Design an integrated system with DNS, Email, HTTP APIs, Keycloak, and a legacy SIS.

**Text-Based Diagram**:
```
[Student App]  
     ↓ (HTTPS/REST)  
[API Gateway] → (OAuth2) → [Keycloak]  
     ↓  
     ├─ Resolve Hostnames → [DNS Resolver Cluster]  
     ├─ Send Email Alerts → [Postfix SMTP] → (SPF/DKIM/DMARC) → External  
     └─ Query SIS → [SOAP-to-REST Adapter] → [Legacy SIS Server]
```

**Model Answer (Bulleted with marking hints)**:
- **DNS (6 marks)**:
  - (2 marks) Architecture: recursive resolver + authoritative zones + caching.
  - (2 marks) Security: DNSSEC explanation and how to validate signatures.
  - (2 marks) Operational: TTL strategy, GeoDNS for latency-aware distribution.
- **Email (6 marks)**:
  - (2 marks) Delivery chain: Postfix + Dovecot + MX/PTR alignment.
  - (2 marks) Security: SPF/DKIM/DMARC examples and how they prevent spoofing.
  - (2 marks) Operations: queue handling, TLS for MTA-MTA and submission.
- **HTTP/REST (8 marks)**:
  - (3 marks) API Gateway responsibilities: auth, rate-limiting, routing.
  - (3 marks) Service design: stateless, error codes, idempotency for POSTs.
  - (2 marks) Observability: metrics, logs, and tracing.
- **IAM & OAuth2 (5 marks)**:
  - (2 marks) Flow selection: justify Authorization Code for web apps.
  - (2 marks) Token handling: refresh, revocation, scopes.
  - (1 mark) Integration: LDAP as user store, Keycloak as IDP.
- **Legacy SIS SOAP (5 marks)**:
  - (3 marks) Adapter design: SOAP-to-REST proxy, transformation and caching.
  - (2 marks) Performance: connection pooling, XML parsing improvements.

---

### **🔹 Q2 — MPESA INTEGRATION (25 MARKS)**
**Scenario**: Integrate mobile payments for fees and cafeteria purchases.

**Text-Based Diagram**:
```
[Student App]  
   ↓ (HTTPS + OAuth2 Token)  
[API Gateway]  
   ↓  
[Payment Microservice] → (HTTPS) → [Safaricom Daraja API]  
                                 ↓  
                           [STK Push to Phone]  
                                 ↓  
                           [PIN Entry → Callback]  
                                 ↓  
                        [Callback Listener] → [ERP/Finance DB]
```

**Model Answer (Bulleted)**:
- **Authentication (4 marks)**:  
  - OAuth2 tokens issued by Keycloak, validated at API Gateway.  
- **Payment Flow (5 marks)**:  
  - HTTPS POST to Daraja with signed JSON, STK Push initiated, user approves.  
- **Daraja Communication (4 marks)**:  
  - TLS 1.2+, idempotent requests, timestamp validation.  
- **Callback Handling (5 marks)**:  
  - Publicly reachable HTTPS endpoint, IP whitelisting, idempotent DB updates.  
- **Security & Reliability (7 marks)**:  
  - Firewall rules, rate limiting, retry logic, redundant service nodes, logging.

---

### **🔹 Q3 — UNIFIED NETWORK ACCESS & MONITORING (25 MARKS)**
**Scenario**: Campus network with 802.1X WiFi, RADIUS, SNMP monitoring, syslog aggregation.

**Text-Based Diagram**:
```
[APs / Switches] → (802.1X/EAPoL) → [RADIUS Server] → (LDAP)  
       ↓                                   ↓  
   (SNMPv3)                         (Syslog → SQL DB)  
       ↓                                   ↓  
 [NMS Server] ←-------------------- (Dashboard/Alerts)
```

**Model Answer (Bulleted)**:
- **802.1X Authentication (5 marks)**:  
  - Supplicant → Authenticator → RADIUS, dynamic VLAN assignment, certificate-based EAP-TLS.  
- **RADIUS AAA (5 marks)**:  
  - FreeRADIUS with LDAP backend, accounting logs for billing/auditing.  
- **SNMPv3 Monitoring (5 marks)**:  
  - Encrypted polls/traps, MIBs for interface stats, CPU, memory.  
- **Syslog Centralization (5 marks)**:  
  - Log severity levels, SQL storage for search, real-time alerting.  
- **Architecture Best Practices (5 marks)**:  
  - VLAN segmentation, NTP synchronization, high availability, firewall ACLs for management traffic.

---

## ✅ **EXAM STRATEGY SUMMARY (How to get full marks)**
- **1. Read the question, then plan (10–20s)**: identify what the examiner is testing (design vs troubleshooting vs explain).
- **2. Use a short heading and bullets**: start with a one-line definition, then 3–6 numbered/bulleted points — each point is usually 1 mark.
- **3. Provide concrete examples**: include commands, config snippets, or short diagrams to show depth.
- **4. Show troubleshooting steps**: give a reproducible check-list: reproduce → isolate → remediate → prevent.
- **5. Finish with trade-offs or monitoring**: mention cost, complexity, and metrics to watch.

---

*File updated: improved explanations, added quick commands/examples, and exam-focused rubrics.*

---
