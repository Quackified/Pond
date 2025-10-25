package clinic.manager;

import clinic.model.Doctor;
import java.util.*;

/**
 * DoctorManager handles all doctor-related operations including
 * adding, updating, deleting, and searching for doctors.
 * Uses a HashMap for efficient doctor lookup by ID.
 */
public class DoctorManager {
    // HashMap for O(1) lookup by doctor ID
    private final Map<Integer, Doctor> doctors;
    
    /**
     * Constructor initializes the doctor storage.
     */
    public DoctorManager() {
        this.doctors = new HashMap<>();
    }
    
    /**
     * Add a new doctor to the system.
     * 
     * @param name Doctor's full name
     * @param specialization Doctor's medical specialization
     * @param phoneNumber Doctor's contact number
     * @param email Doctor's email address
     * @param availableDays List of days doctor is available
     * @param startTime Doctor's starting work time
     * @param endTime Doctor's ending work time
     * @return The newly created Doctor object
     */
    public Doctor addDoctor(String name, String specialization, String phoneNumber,
                           String email, List<String> availableDays, String startTime,
                           String endTime) {
        Doctor doctor = new Doctor(name, specialization, phoneNumber, email,
                                  availableDays, startTime, endTime);
        doctors.put(doctor.getId(), doctor);
        return doctor;
    }
    
    /**
     * Get a doctor by their ID.
     * 
     * @param id Doctor ID to look up
     * @return Doctor object if found, null otherwise
     */
    public Doctor getDoctorById(int id) {
        return doctors.get(id);
    }
    
    /**
     * Get all doctors in the system.
     * 
     * @return List of all doctors
     */
    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctors.values());
    }
    
    /**
     * Get only available doctors.
     * 
     * @return List of available doctors
     */
    public List<Doctor> getAvailableDoctors() {
        List<Doctor> available = new ArrayList<>();
        for (Doctor doctor : doctors.values()) {
            if (doctor.isAvailable()) {
                available.add(doctor);
            }
        }
        return available;
    }
    
    /**
     * Search for doctors by name (case-insensitive partial match).
     * 
     * @param name Name or partial name to search for
     * @return List of matching doctors
     */
    public List<Doctor> searchDoctorsByName(String name) {
        List<Doctor> results = new ArrayList<>();
        String searchTerm = name.toLowerCase();
        
        for (Doctor doctor : doctors.values()) {
            if (doctor.getName().toLowerCase().contains(searchTerm)) {
                results.add(doctor);
            }
        }
        
        return results;
    }
    
    /**
     * Search for doctors by specialization (case-insensitive partial match).
     * 
     * @param specialization Specialization to search for
     * @return List of matching doctors
     */
    public List<Doctor> searchDoctorsBySpecialization(String specialization) {
        List<Doctor> results = new ArrayList<>();
        String searchTerm = specialization.toLowerCase();
        
        for (Doctor doctor : doctors.values()) {
            if (doctor.getSpecialization().toLowerCase().contains(searchTerm)) {
                results.add(doctor);
            }
        }
        
        return results;
    }
    
    /**
     * Update doctor information.
     * 
     * @param id Doctor ID to update
     * @param name New name (null to keep existing)
     * @param specialization New specialization (null to keep existing)
     * @param phoneNumber New phone number (null to keep existing)
     * @param email New email (null to keep existing)
     * @param availableDays New available days (null to keep existing)
     * @param startTime New start time (null to keep existing)
     * @param endTime New end time (null to keep existing)
     * @return true if doctor was found and updated, false otherwise
     */
    public boolean updateDoctor(int id, String name, String specialization,
                               String phoneNumber, String email, List<String> availableDays,
                               String startTime, String endTime) {
        Doctor doctor = doctors.get(id);
        if (doctor == null) {
            return false;
        }
        
        // Update only non-null fields
        if (name != null) doctor.setName(name);
        if (specialization != null) doctor.setSpecialization(specialization);
        if (phoneNumber != null) doctor.setPhoneNumber(phoneNumber);
        if (email != null) doctor.setEmail(email);
        if (availableDays != null) doctor.setAvailableDays(availableDays);
        if (startTime != null) doctor.setStartTime(startTime);
        if (endTime != null) doctor.setEndTime(endTime);
        
        return true;
    }
    
    /**
     * Set doctor availability status.
     * 
     * @param id Doctor ID
     * @param available Availability status to set
     * @return true if doctor was found and updated, false otherwise
     */
    public boolean setDoctorAvailability(int id, boolean available) {
        Doctor doctor = doctors.get(id);
        if (doctor == null) {
            return false;
        }
        doctor.setAvailable(available);
        return true;
    }
    
    /**
     * Delete a doctor from the system.
     * 
     * @param id Doctor ID to delete
     * @return true if doctor was found and deleted, false otherwise
     */
    public boolean deleteDoctor(int id) {
        return doctors.remove(id) != null;
    }
    
    /**
     * Get the total number of doctors in the system.
     * 
     * @return Count of doctors
     */
    public int getDoctorCount() {
        return doctors.size();
    }
    
    /**
     * Check if a doctor exists by ID.
     * 
     * @param id Doctor ID to check
     * @return true if doctor exists, false otherwise
     */
    public boolean doctorExists(int id) {
        return doctors.containsKey(id);
    }
    
    /**
     * Get unique list of all specializations in the system.
     * 
     * @return Set of specializations
     */
    public Set<String> getAllSpecializations() {
        Set<String> specializations = new HashSet<>();
        for (Doctor doctor : doctors.values()) {
            specializations.add(doctor.getSpecialization());
        }
        return specializations;
    }
}
