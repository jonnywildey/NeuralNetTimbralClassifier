TimbreWav
=========

Java implementation of a combination of a lightweight DSP library and an ANN for the purposes of timbre classification. 
ANN is very customisable, including modular layers, multiple selection functions, confusion matrices, 
non-binary matthews coefficient calculation, pattern shuffling, pattern selection, batch runs and graphs.

DSP library currently consists of:

Wave read/write: Read a Wave file and obtain Header info, signal info and meta data. This also allows writing of signal data and metadata into a Wave file. Can read and write up to 32bit mono and stereo files. 

Signal: The basic object for signal manipulation. Multi-channel 64 bit floating point precision. Also includes methods for graph generation and adherence to fixed-point amplitude constrains.

FFT: Static fourier transform and inverse fourier transform methods. Also contains methods for easily converting a signal into an FFT signal, in addition to graphing features.

FrameFFT: Contains methods for allowing easy conversion from a signal to a windowed (or framed) FFT signal

DCT: static DCT methods and methods for easy conversion from a signal to a DCT signal

Fundamental: detection of the fundamental of a sound. (Simple algorithm but works quite well).

Pitch Shift

Filter: Implementation of 2nd order filters in the forms of:
    High Pass:
    Low Pass:
    Peak:
    Band Pass:
    Notch;

Mixing and gain:
M/S Encoding/Decoding
Summing
Gain adjustment by Peak and RMS
Bit Rate Conversion

Audio Generation:
Generate Sine, Saw, Square, Triangle waves
Generate white noise, pink noise, tape hiss

Developed for Java 1.6

